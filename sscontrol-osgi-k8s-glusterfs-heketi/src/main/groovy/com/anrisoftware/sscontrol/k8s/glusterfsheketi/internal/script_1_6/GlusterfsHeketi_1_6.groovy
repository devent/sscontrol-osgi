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
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
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

    TemplateResource installResource

    TemplateResource gkdeployResource

    TemplateResource storageClassResource

    @Inject
    def setTemplates(TemplatesFactory factory) {
        def t = factory.create 'GlusterfsHeketi_1_6_Templates'
        this.installResource = t.getResource 'install'
        this.gkdeployResource = t.getResource 'gkdeploy'
        this.storageClassResource = t.getResource 'storage_class'
    }

    @Override
    def run() {
        setupDefaults()
        checkAptPackages() ?: installAptPackages()
        installPackagesNodes()
        installHeketi()
        installGlusterKubernetesDeploy()
        installGlusterfsHeketi()
        deployStorageClass()
    }

    def setupDefaults() {
        GlusterfsHeketi service = service
        if (!service.namespace) {
            service.namespace = defaultNamespace
        }
        if (!service.storage.name) {
            service.storage.name = defaultStorageClassName
        }
        if (service.storage.isDefault == null) {
            service.storage.isDefault = defaultStorageClassIsDefault
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

    /**
     * Installs needed packages on the nodes.
     */
    def installPackagesNodes() {
        GlusterfsHeketi service = service
        def nodes = service.nodes
        def list = findService 'ssh', service.nodes
        assertThat "nodes=0", list.size(), greaterThan(0)
        list.each { ssh ->
            ssh.targets.each { target ->
                if (nodeBackportsPackages.size() > 0) {
                    log.debug 'Install backport packages for node {}', target
                    if (!checkAptPackage(target: target, package: nodeBackportsPackages)) {
                        addAptBackportsRepository target: target
                        installAptBackportsPackages target: target, packages: nodeBackportsPackages
                    }
                }
                if (nodePackages.size() > 0) {
                    log.debug 'Install packages for node {}', target
                    if (!checkAptPackage(target: target, package: nodePackages)) {
                        installAptPackages target: target, packages: nodePackages
                    }
                }
                if (nodeKernelModules.size() > 0) {
                    log.debug 'Setup kernel modules for node {}', target
                    shell target: target, privileged: true, st: """
<parent.nodeKernelModules:{m|modprobe <m>};separator="\\n">
""" call()
                    replace privileged: true, dest: '/etc/modules' with {
                        line "s/(?m)^#?dm_thin_pool/dm_thin_pool/"
                        it
                    }()
                }
            }
        }
    }

    def installGlusterfsHeketi() {
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
            deployGlusterfsHeketi dir, cluster, glusterfsScript
        }
        fromRepository.run()
    }

    def installHeketi() {
        log.info 'Installs Heketi.'
        GlusterfsHeketi service = this.service
        copy src: heketiArchive, hash: heketiArchiveHash, dest: "/tmp", direct: true, timeout: timeoutLong call()
        shell resource: installResource, name: 'installHeketiCmd' call()
    }

    def installGlusterKubernetesDeploy() {
        log.info 'Installs gluster-kubernetes-deploy.'
        def tmp = createTmpDir()
        try {
            def name = glusterKubernetesDeployArchiveFile
            copy src: glusterKubernetesDeployArchive, hash: glusterKubernetesDeployArchiveHash, dest: "$tmp/$name", direct: true, timeout: timeoutLong call()
            shell timeout: timeoutMiddle, resource: installResource, name: 'installGlusterKubernetesDeployCmd',
            vars: [parentDir: tmp] call()
        } finally {
            deleteTmpFile tmp
        }
    }

    def deployGlusterfsHeketi(File dir, HostServiceScript cluster, GlusterfsHeketi_1_6 glusterfsScript) {
        def topologyFile = glusterfsScript.deployTopologyFile()
        try {
            shell timeout: timeoutMiddle, resource: gkdeployResource, name: 'gkdeployCmd',
            vars: [
                glusterfsScript: glusterfsScript,
                templatesDir: dir,
                topologyFile: topologyFile
            ] call()
        } finally {
            deleteTmpFile topologyFile as File
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

    /**
     * Deploys the StorageClass for heketi.
     */
    def deployStorageClass() {
        GlusterfsHeketi service = service
        if (!deployDefaultStorageClass) {
            return
        }
        def target = service.cluster.cluster.target
        if (!service.storage.restAddress) {
            def ret = shell outString: true, target: target, resource: installResource, name: 'getHeketiAddress', vars: [:] call()
            def address = ret.out[0..-2]
            service.storage.restAddress = address
        }
        def tmp = createTmpFile target: target
        try {
            template target: target, resource: storageClassResource, name: 'storageClass', dest: tmp, vars: [:] call()
            shell target: target, "${kubectlCmd} apply -f $tmp" call()
        } finally {
            deleteTmpFile file: tmp, target: target
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

    URI getGlusterKubernetesDeployArchive() {
        properties.getURIProperty 'gluster_kubernetes_deploy_archive', defaultProperties
    }

    List getNodeBackportsPackages() {
        properties.getListProperty "node_backports_packages", defaultProperties
    }

    List getNodePackages() {
        properties.getListProperty "node_packages", defaultProperties
    }

    List getNodeKernelModules() {
        properties.getListProperty "node_kernel_modules", defaultProperties
    }

    String getGlusterKubernetesDeployArchiveFile() {
        properties.getProperty "gluster_kubernetes_deploy_archive_file", defaultProperties
    }

    String getGlusterKubernetesDeployArchiveName() {
        getBaseName(getBaseName(glusterKubernetesDeployArchive.toString()))
    }

    String getGlusterKubernetesDeployArchiveHash() {
        properties.getProperty 'gluster_kubernetes_deploy_archive_hash', defaultProperties
    }

    File getGlusterKubernetesDeployCommand() {
        getFileProperty 'gluster_kubernetes_deploy_command', optDir, defaultProperties
    }

    String getHeketiArchive() {
        properties.getProperty 'heketi_archive', defaultProperties
    }

    URI getHeketiArchiveHash() {
        properties.getURIProperty 'heketi_archive_hash', defaultProperties
    }

    boolean getDeployDefaultStorageClass() {
        properties.getBooleanProperty 'deploy_default_storage_class', defaultProperties
    }

    String getDefaultStorageClassName() {
        properties.getProperty 'default_storage_class_name', defaultProperties
    }

    boolean getDefaultStorageClassIsDefault() {
        properties.getBooleanProperty 'default_storage_class_is_default', defaultProperties
    }

    File getKubectlCmd() {
        getFileProperty 'kubectl_command'
    }

    @Override
    def getLog() {
        log
    }
}
