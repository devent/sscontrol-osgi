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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6

import static org.apache.commons.io.FilenameUtils.getBaseName
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.Templates
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.external.GlusterfsHeketi
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

/**
 * glusterfs-heketi for Kubernetes 1.6.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class GlusterfsHeketi_1_6 extends ScriptBase {

    @Inject
    GlusterfsHeketi_1_6_Properties debianPropertiesProvider

    Templates templates

    @Override
    def run() {
        setupDefaults()
        checkAptPackages() ?: installAptPackages()
        installGlusterKubernetesDeploy()
        GlusterfsHeketi service = service
        def args = [:]
        args.targets = service.targets
        args.clusters = service.clusters
        args.repos = service.repos
        def fromRepositoryService = findAvailableService 'from-repository' create args
        fromRepositoryService.vars << service.vars
        fromRepositoryService.serviceProperties = service.serviceProperties
        def fromRepository = createScript 'from-repository', fromRepositoryService
        def glusterfsScript = this
        fromRepository.metaClass.kubeFiles = {File dir, HostServiceScript cluster ->
            def topologyFile = glusterfsScript.deployTopologyFile()
            try {
                shell timeout: timeoutMiddle, """\
set -e
echo 'Run with topology ${topologyFile}:'
cat ${topologyFile}
${glusterfsScript.glusterKubernetesDeployCommand} -g --yes \\
-c kubectl \\
-t $dir \\
--admin-key ${glusterfsScript.service.admin.key} \\
--user-key ${glusterfsScript.service.user.key} \\
-n ${glusterfsScript.service.namespace} \\
--daemonset-label ${glusterfsScript.service.labelName} \\
${topologyFile}
""" call()
            } finally {
                deleteTmpFile topologyFile as File
            }
        }
        fromRepository.run()
    }

    def setupDefaults() {
        GlusterfsHeketi service = service
        if (!service.namespace) {
            service.namespace = defaultNamespace
        }
        def vars = service.vars
        if (!vars.heketi) {
            vars.heketi = [:]
            if (!vars.heketi.image) {
                vars.heketi.image = [:]
                if (!vars.heketi.image.name) {
                    vars.heketi.image.name = heketiImageName
                }
                if (!vars.heketi.image.version) {
                    vars.heketi.image.version = heketiImageVersion
                }
            }
            if (!vars.heketi.snapshot) {
                vars.heketi.snapshot = [:]
                if (!vars.heketi.snapshot.limit) {
                    vars.heketi.snapshot.limit = heketiSnapshotLimit
                }
            }
            if (!vars.gluster) {
                vars.gluster = [:]
                vars.gluster.image = [:]
                if (!vars.gluster.image.name) {
                    vars.gluster.image.name = glusterImageName
                }
                if (!vars.gluster.image.version) {
                    vars.gluster.image.version = glusterImageVersion
                }
            }
        }
    }

    def installGlusterKubernetesDeploy() {
        log.info 'Installs gluster-kubernetes-deploy.'
        def tmp = createTmpDir()
        try {
            copy src: archive, hash: archiveHash, dest: "$tmp", direct: true, timeout: timeoutLong call()
            shell timeout: timeoutMiddle, """\
set -e
cd '$tmp'
unzip "$archiveFile"
sudo rm -rf '$optDir/gluster-kubernetes-master'
sudo cp -r gluster-kubernetes-master '$optDir'
""" call()
        } finally {
            deleteTmpFile tmp
        }
    }

    /**
     * Deploys the topology json file.
     * @return the path of the file.
     */
    String deployTopologyFile() {
        GlusterfsHeketi service = service
        def topology = JsonOutput.toJson(service.topology)
        def tmp = createTmpFile()
        try {
            copyString str: topology, dest: tmp
            return tmp
        }  catch (e) {
            deleteTmpFile tmp
            throw e
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    String getDefaultNamespace() {
        properties.getProperty 'default_namespace', defaultProperties
    }

    String getHeketiImageName() {
        properties.getProperty 'default_heketi_image_name', defaultProperties
    }

    String getHeketiImageVersion() {
        properties.getProperty 'default_heketi_image_version', defaultProperties
    }

    int getHeketiSnapshotLimit() {
        properties.getNumberProperty 'default_heketi_snapshot_limit', defaultProperties
    }

    String getGlusterImageName() {
        properties.getProperty 'default_gluster_image_name', defaultProperties
    }

    String getGlusterImageVersion() {
        properties.getProperty 'default_gluster_image_version', defaultProperties
    }

    URI getArchive() {
        properties.getURIProperty 'gluster_kubernetes_deploy_archive', defaultProperties
    }

    String getArchiveFile() {
        FilenameUtils.getName(archive.toString())
    }

    String getArchiveName() {
        getBaseName(getBaseName(archive.toString()))
    }

    String getArchiveHash() {
        properties.getProperty 'gluster_kubernetes_deploy_archive_hash', defaultProperties
    }

    File getGlusterKubernetesDeployCommand() {
        getFileProperty 'gluster_kubernetes_deploy_command', optDir, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
