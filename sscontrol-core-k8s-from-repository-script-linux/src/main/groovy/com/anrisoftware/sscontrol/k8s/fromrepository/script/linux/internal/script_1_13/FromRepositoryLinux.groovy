/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.command.shell.linux.openssh.external.find.FindFilesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.fromrepository.service.external.FromRepository

import groovy.io.FileType
import groovy.util.logging.Slf4j

/**
 * From repository service for Kubernetes.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FromRepositoryLinux extends ScriptBase {

    @Inject
    FromRepositoryLinuxProperties linuxPropertiesProvider

    @Inject
    Map<String, TemplateParser> templateParsers

    @Inject
    FindFilesFactory findFilesFactory

    KubectlClusterLinux kubectlClusterLinux

    @Inject
    void setKubectlClusterLinuxFactory(KubectlClusterLinuxFactory factory) {
        this.kubectlClusterLinux = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        FromRepository service = service
        assertThat "clusters=0 for $service", service.clusterHosts.size(), greaterThan(0)
        File dir = getState "${service.repo.type}-${service.repo.repo.group}-dir"
        assertThat "checkout-dir=null for $service", dir, notNullValue()
        setupDefaults()
        try {
            parseTemplateFiles dir
            buildDocker dir
            kubeFiles dir
        } finally {
            shell "rm -rf $dir" call()
        }
    }

    def setupDefaults() {
        setupDefaultCrds()
    }

    def setupDefaultCrds() {
        FromRepository service = service
        ignoreCrds.each { val ->
            String[] split = val.split(":")
            String kind = split[0]
            String version = ".*"
            if (split.length > 1) {
                version = split[1]
            }
            service.crds([kind: kind, version: version])
        }
    }

    def buildDocker(File dir) {
        FromRepository service = service
        if (!service.registry) {
            return
        }
        def files = createCmd findFilesFactory, chdir: dir, patterns: dockerfileFilesPatterns call()
        def templatesPatterns = templateParsers.keySet()
        files = files.findAll { String file ->
            if (file.empty) {
                return false
            }
            for (String pattern : templatesPatterns) {
                if (file.endsWith(".$pattern")) {
                    return false
                }
            }
            return true
        }
        if (files.size() == 0) {
            return
        }
        def s = findService 'registry-docker', service.registry.registry.group
        def script = createScript 'registry-docker', s[0]
        def vars = [:]
        vars.repo = service.repo.repo
        vars.dir = dir
        vars = script.setupDockerDefaults vars
        vars = script.deployDockerConfig vars
        files.each { String file ->
            Map v = new HashMap(vars)
            v.dockerFile = file
            script.dockerBuild v
            script.dockerPush v
        }
    }

    /**
     * Apply kubectl files.
     */
    def kubeFiles(File dir) {
        FromRepository service = this.service
        createCmd findFilesFactory, chdir: dir, suffix: kubectlFilesPatterns call() each {
            if (!StringUtils.isBlank(it)) {
                try {
                    readApplyManifestTmpFile dir, it
                } catch (e) {
                    throw new ApplyManifestException(e, dir, it)
                }
            }
        }
    }

    def readApplyManifestTmpFile(File dir, def name) {
        withLocalTempFile name, { File tmp ->
            readApplyManifest dir, name, tmp
        }
    }

    def readApplyManifest(File dir, def name, File tmp) {
        fetch src: "$dir/$name", dest: tmp call()
        def s = FileUtils.readFileToString(tmp, charset)
        if (s.isBlank()) {
            log.debug 'Skip empty manifest {}/{}', dir, name
            return
        }
        withRemoteTempFile { File destTmp ->
            copy src: tmp, dest: destTmp call()
            log.debug 'Apply manifest {}/{}', dir, name
            log.trace 'Apply manifest {}/{}: ```\n{}```', dir, name, s
            FromRepository service = service
            if (service.destination && !service.dryrun) {
                deployToDestination name, destTmp, service.destination
            } else {
                kubectlClusterLinux.runKubectl chdir: dir, args: "apply ${service.dryrun ? "--dry-run=true" : ""} -f $destTmp"
            }
        }
    }

    def parseTemplateFiles(File dir) {
        FromRepository service = this.service
        def args = [parent: this, vars: service.vars]
        findTemplateFiles dir, { String pattern, String fileName ->
            def parser = templateParsers[pattern]
            copyRepoAndParse dir, fileName, args, parser
            return true
        }
    }

    def findTemplateFiles(File dir, def callback) {
        FromRepository service = this.service
        shell "rm -rf $dir/.git" call()
        def files = createCmd findFilesFactory, chdir: dir, suffix: templateParsers.keySet() call()
        templateParsers.keySet().each { String pattern ->
            files.findAll { it =~ /(?m)\.${pattern}$/ }.any { String fileName ->
                return callback(pattern, fileName)
            }
        }
    }

    def copyRepoAndParse(File dir, String fileName, Map args, TemplateParser parser) {
        log.info 'Parse templates in {}', dir
        def tmpdir = File.createTempDir('robobee', null)
        try {
            fetch recursive: true, src: "$dir", dest: "$tmpdir" call()
            def targetdirName
            tmpdir.eachFileMatch FileType.DIRECTORIES, ~/${dir.name}/, { targetdirName = it.name }
            def targetdir = new File(tmpdir, targetdirName)
            targetdir.eachFileMatch FileType.FILES, ~/(?m)^.*\.${parser.templateName}$/, {
                log.debug 'Parse template {}/{}', dir, it.name
                if (!parser.isKubeFile(it.name)) {
                    return
                }
                def parsedFileName = parser.getFilename it.name
                def s = parser.parseFile(targetdir, it.name, args, charset)
                def tmpdest = File.createTempFile('robobee', null)
                try {
                    FileUtils.write tmpdest, s, charset
                    log.trace 'Upload parsed file {} for {}: \n``{}\'\'', this, tmpdest, s
                    copy src: tmpdest, dest: "$dir/$parsedFileName" call()
                } finally {
                    tmpdest.delete()
                }
            }
        } finally {
            tmpdir.deleteDir()
        }
    }

    /**
     * Deploy manifests to the destination directory.
     */
    def deployToDestination(String name, File destTmp, String destination) {
        checkManifestByKubectl name, destTmp
        shell privileged: true, """
mkdir -p ${destination}
mv ${destTmp} ${destination}/${name}
""" call()
    }

    /**
     * <p>Checks the manifest by using kubectl --dry-run.
     * <p>If the encountered manifest is returning errors in the pattern of
     * 'no matches for kind "ServiceMonitor" in version "monitoring.coreos.com/v1"'
     * and the kind and version is in the ignore_crds list then the check of the manifest
     * is skipped.
     */
    def checkManifestByKubectl(String name, File destTmp) {
        FromRepository service = this.service
        def p = shell exitCodes: [0, 1] as int[], outString: true, errString: true,
        "kubectl create --dry-run -f ${destTmp}" call()
        if (p.exitValue == 1) {
            def found = service.crds.find { p.err.contains("no matches for kind \"${it.kind}\" in version \"${it.version}\"") }
            if (found) {
                log.debug "Ignore unknown CRD: {}", found
            } else {
                def manifest = shell outString: true, "cat ${destTmp}" call() out
                throw new ManifestErrorsException(name, p.out, p.err, manifest)
            }
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    List getKubectlFilesPatterns() {
        getScriptListProperty 'kubectl_files_patterns'
    }

    List getDockerfileFilesPatterns () {
        getScriptListProperty 'dockerfiles_files_patterns'
    }

    /**
     * Returns the property {@code ignore_crds}.
     */
    List getIgnoreCrds() {
        getScriptListProperty 'ignore_crds'
    }

    @Override
    def getLog() {
        log
    }
}
