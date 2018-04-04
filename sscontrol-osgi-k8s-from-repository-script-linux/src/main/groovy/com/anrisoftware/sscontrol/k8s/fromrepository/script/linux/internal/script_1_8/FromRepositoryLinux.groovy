/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

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
 * From repository service for Kubernetes 1.5.
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
        try {
            parseTemplateFiles dir
            buildDocker dir
            kubeFiles dir
        } finally {
            shell "rm -rf $dir" call()
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
        withRemoteTempFile { File destTmp ->
            copy src: tmp, dest: destTmp call()
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
        shell privileged: true, """
mkdir -p ${destination}
mv ${destTmp} ${destination}/${name}
""" call()
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

    @Override
    def getLog() {
        log
    }
}
