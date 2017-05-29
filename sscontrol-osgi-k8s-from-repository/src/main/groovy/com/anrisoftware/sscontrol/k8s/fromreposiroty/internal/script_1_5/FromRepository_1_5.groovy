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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.Templates
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.FromRepository
import com.anrisoftware.sscontrol.shell.linux.openssh.external.find.FindFilesFactory
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService

import groovy.io.FileType
import groovy.util.logging.Slf4j

/**
 * From repository service for Kubernetes 1.5.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FromRepository_1_5 extends ScriptBase {

    @Inject
    FromRepository_1_5_Properties debianPropertiesProvider

    @Inject
    HostServiceScriptService k8sCluster_1_5_Linux_Service

    @Inject
    Map<String, TemplateParser> templateParsers

    @Inject
    FindFilesFactory findFilesFactory

    Templates templates

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create('MonitoringClusterHeapsterInfluxdbGrafana_1_5_Templates')
    }

    @Override
    def run() {
        FromRepository service = service
        def cluster = k8sCluster_1_5_Linux_Service.create(scriptsRepository, service, target, threads, scriptEnv)
        cluster.uploadCertificates credentials: service.cluster.cluster.credentials, clusterName: service.cluster.cluster.cluster.name
        File dir = getState "${service.repo.type}-${service.repo.repo.group}-dir"
        try {
            buildDocker(dir)
            kubeTemplateFiles(dir, cluster)
            kubeFiles(dir, cluster)
        } finally {
            shell "rm -rf $dir" call()
        }
    }

    def buildDocker(File dir) {
        FromRepository service = service
        def files = createCmd findFilesFactory, chdir: dir, patterns: dockerfileFilesPatterns call()
        if (files.size() == 0) {
            return
        }
        def script = createScript 'registry-docker'
        def vars = [:]
        vars.repo = service.repo.repo
        vars.dir = dir
        vars = script.setupDockerDefaults vars
        vars = script.deployDockerConfig vars
        files.each { String file ->
            Map v = new HashMap(vars)
            v.dockerFile = file
            script.dockerBuild v
        }
    }

    /**
     * Apply kubectl files.
     */
    def kubeFiles(File dir, HostServiceScript cluster) {
        FromRepository service = this.service
        createCmd findFilesFactory, chdir: dir, suffix: kubectlFilesPatterns call() each {
            if (!StringUtils.isBlank(it)) {
                try {
                    cluster.runKubectl chdir: dir, service: service, cluster: service.cluster, args: "apply -f $it"
                } catch (e) {
                    File tmp = File.createTempFile(it, null)
                    fetch src: "$dir/$it", dest: tmp call()
                    def s = FileUtils.readFileToString(tmp, charset)
                    tmp.delete()
                    throw new ApplyManifestException(e, dir, it, s)
                }
            }
        }
    }

    /**
     * Parse template files.
     */
    def kubeTemplateFiles(File dir, HostServiceScript cluster) {
        FromRepository service = this.service
        shell "rm -rf $dir/.git" call()
        def files = createCmd findFilesFactory, chdir: dir, suffix: templateParsers.keySet() call()
        def args = [parent: this, vars: service.vars]
        templateParsers.keySet().each { pattern ->
            files.findAll { it =~ /(?m)\.${pattern}$/ }.any {
                def parser = templateParsers[pattern]
                if (parser.needCopyRepo) {
                    copyRepoAndParse dir, it, args, parser
                    return true
                } else {
                    parseTemplate dir, it, args, parser
                    return false
                }
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
                    copy src: tmpdest, dest: "$dir/$parsedFileName" call()
                } finally {
                    tmpdest.delete()
                }
            }
        } finally {
            tmpdir.deleteDir()
        }
    }

    def parseTemplate(File dir, String fileName, Map args, TemplateParser parser) {
        log.info 'Parse template {}/{}', dir, fileName
        def tmpdir = File.createTempDir('robobee', null)
        try {
            fetch src: "$dir/$fileName", dest: "$tmpdir/$fileName" call()
            def s = parser.parseFile(tmpdir, fileName, args, charset)
            def parsedFileName = parser.getFilename fileName
            def tmpdest = File.createTempFile('robobee', null)
            try {
                FileUtils.write tmpdest, s, charset
                copy src: tmpdest, dest: "$dir/$parsedFileName" call()
            } finally {
                tmpdest.delete()
            }
        } finally {
            tmpdir.deleteDir()
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    List getKubectlFilesPatterns() {
        properties.getListProperty 'kubectl_files_patterns', defaultProperties
    }

    List getDockerfileFilesPatterns () {
        properties.getListProperty 'dockerfiles_files_patterns', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
