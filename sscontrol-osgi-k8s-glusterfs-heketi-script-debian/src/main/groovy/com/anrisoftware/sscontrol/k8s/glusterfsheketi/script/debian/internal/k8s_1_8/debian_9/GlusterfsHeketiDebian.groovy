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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.k8s_1_8.debian_9

import static org.apache.commons.io.FilenameUtils.getBaseName
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.fromrepository.service.external.FromRepository
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.GlusterfsHeketi
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.ssh.external.TargetsListFactory
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

/**
 * glusterfs-heketi.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class GlusterfsHeketiDebian extends ScriptBase {

    @Inject
    GlusterfsHeketiDebianProperties debianPropertiesProvider

    @Inject
    TargetsListFactory targetsFactory

    @Inject
    GlusterfsHeketiUfwDebianFactory ufwFactory

    TemplateResource installResource

    TemplateResource gkdeployResource

    TemplateResource storageClassResource

    DebianUtils debian

    @Inject
    void setDebian(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    def setTemplates(TemplatesFactory factory) {
        def t = factory.create 'GlusterfsHeketi_1_8_Debian_9_Templates'
        this.installResource = t.getResource 'install'
        this.gkdeployResource = t.getResource 'gkdeploy'
        this.storageClassResource = t.getResource 'storage_class'
    }

    @Override
    def run() {
        setupDefaults()
        ufwFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        debian.installPackages()
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
        if (!service.storage.restAddress) {
            service.storage.restAddress = scriptProperties.default_rest_address
        }
        if (!service.minBrickSizeGb) {
            service.minBrickSizeGb = scriptNumberProperties.default_min_brick_size_gb
        }
        if (!service.maxBrickSizeGb) {
            service.maxBrickSizeGb = scriptNumberProperties.default_max_brick_size_gb
        }
        if (!service.serviceAddress) {
            service.serviceAddress = scriptProperties.default_service_address
        }
        def vars = service.vars
        vars.heketi = vars.heketi ?: [:]
        vars.heketi.clusterIP = service.serviceAddress
        vars.heketi.image = vars.heketi.image ?: [:]
        vars.heketi.image.name = vars.heketi.image.name ?: scriptProperties.default_heketi_image_name
        vars.heketi.image.version = vars.heketi.image.version ?: scriptProperties.default_heketi_image_version
        vars.heketi.limits = vars.heketi.limits ?: [:]
        vars.heketi.limits.cpu = vars.heketi.limits.cpu ?: scriptProperties.default_heketi_limits_cpu
        vars.heketi.limits.memory = vars.heketi.limits.memory ?: scriptProperties.default_heketi_limits_memory
        vars.heketi.requests = vars.heketi.requests ?: [:]
        vars.heketi.requests.cpu = vars.heketi.requests.cpu ?: scriptProperties.default_heketi_requests_cpu
        vars.heketi.requests.memory = vars.heketi.requests.memory ?: scriptProperties.default_heketi_requests_memory
        vars.heketi.snapshot = vars.heketi.snapshot ?: [:]
        vars.heketi.snapshot.limit = vars.heketi.snapshot.limit ?: scriptNumberProperties.default_heketi_snapshot_limit
        vars.heketi.allowOnMaster = vars.heketi.allowOnMaster != null ?: scriptBooleanProperties.default_heketi_allowonmaster
        vars.gluster = vars.gluster ?: [:]
        vars.gluster.image = vars.gluster.image ?: [:]
        vars.gluster.image.name = vars.gluster.image.name ?: scriptProperties.default_gluster_image_name
        vars.gluster.image.version = vars.gluster.image.version ?: scriptProperties.default_gluster_image_version
        vars.gluster.limits = vars.gluster.limits ?: [:]
        vars.gluster.limits.cpu = vars.gluster.limits.cpu ?: scriptProperties.default_gluster_limits_cpu
        vars.gluster.limits.memory = vars.gluster.limits.memory ?: scriptProperties.default_gluster_limits_memory
        vars.gluster.requests = vars.gluster.requests ?: [:]
        vars.gluster.requests.cpu = vars.gluster.requests.cpu ?: scriptProperties.default_gluster_requests_cpu
        vars.gluster.requests.memory = vars.gluster.requests.memory ?: scriptProperties.default_gluster_requests_memory
        vars.gluster.allowOnMaster = vars.gluster.allowOnMaster != null ?: scriptBooleanProperties.default_gluster_allowonmaster
    }

    /**
     * Installs needed packages on the nodes.
     */
    def installPackagesNodes() {
        GlusterfsHeketi service = service
        def nodes = nodesTargets
        assertThat "nodes=0", nodes.size(), greaterThan(0)
        nodes.each { target ->
            debian.addPackagesRepository target: target, key: nodePackagesRepositoryKey, url: nodePackagesRepositoryUrl, file: nodePackagesRepositoryListFile
            debian.installPackages target: target, packages: nodePackages, update: true
            debian.enableModules nodeKernelModules, target
        }
    }

    List getNodesTargets() {
        GlusterfsHeketi service = this.service
        targetsFactory.create(service, scriptsRepository, "nodes", this).nodes
    }

    def installGlusterfsHeketi() {
        GlusterfsHeketi service = service
        def args = [:]
        args.targets = service.targets
        args.clusters = service.clusters
        args.repos = service.repos
        FromRepository fromRepositoryService = findAvailableService 'from-repository' create args
        fromRepositoryService.vars << service.vars
        fromRepositoryService.serviceProperties = service.serviceProperties
        def fromRepository = createScript 'from-repository', fromRepositoryService
        def glusterfsScript = this
        fromRepository.metaClass.kubeFiles = {File dir ->
            def cluster = fromRepositoryService.clusterHost
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
        withRemoteTempDir { File tmp ->
            def name = glusterKubernetesDeployArchiveFile
            copy src: glusterKubernetesDeployArchive, hash: glusterKubernetesDeployArchiveHash, dest: "$tmp/$name", direct: true, timeout: timeoutLong call()
            shell timeout: timeoutMiddle, resource: installResource, name: 'installGlusterKubernetesDeployCmd',
            vars: [parentDir: tmp] call()
        }
    }

    def deployGlusterfsHeketi(File dir, ClusterHost cluster, GlusterfsHeketiDebian glusterfsScript) {
        withRemoteTempFile { File topologyFile ->
            glusterfsScript.deployTopologyFile(topologyFile)
            shell target: cluster.target, timeout: timeoutMiddle, resource: gkdeployResource, name: 'gkdeployCmd',
            vars: [
                glusterfsScript: glusterfsScript,
                templatesDir: dir,
                topologyFile: topologyFile
            ] call()
        }
    }

    /**
     * Deploys the topology json file.
     * @return the path of the file.
     */
    void deployTopologyFile(File file) {
        GlusterfsHeketi service = service
        def topology = JsonOutput.toJson(service.topology)
        copyString str: topology, dest: file
    }

    /**
     * Deploys the StorageClass for heketi.
     */
    def deployStorageClass() {
        GlusterfsHeketi service = service
        if (!deployDefaultStorageClass) {
            return
        }
        def target = service.target
        if (!service.storage.restAddress) {
            def ret = shell outString: true, target: target, resource: installResource, name: 'getHeketiAddress', vars: [:] call()
            def address = ret.out[0..-2]
            service.storage.restAddress = address
        }
        if (!service.storage.restUser) {
            service.storage.restUser = "admin"
        }
        if (!service.storage.restKey) {
            service.storage.restKey = service.admin.key
        }
        def tmp = createTmpFile target: target
        try {
            template target: target, resource: storageClassResource, name: 'storageClass', dest: tmp, vars: [:] call()
            shell target: target, resource: storageClassResource, name: 'createStorageClass', vars: [file: tmp] call()
        } finally {
            deleteTmpFile file: tmp, target: target
        }
        shell target: target, resource: storageClassResource, name: 'testStorageClass', vars: [:] call()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    String getDefaultNamespace() {
        properties.getProperty 'default_namespace', defaultProperties
    }

    URI getGlusterKubernetesDeployArchive() {
        properties.getURIProperty 'gluster_kubernetes_deploy_archive', defaultProperties
    }

    List getNodeBackportsPackages() {
        properties.getListProperty "node_backports_packages", defaultProperties
    }

    List getNodePackages() {
        getScriptPackagesVersionsProperty "node_packages"
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
        getScriptFileProperty 'gluster_kubernetes_deploy_command', optDir, defaultProperties
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

    boolean getDebugGkdeploy() {
        properties.getBooleanProperty 'debug_gk_deploy', defaultProperties
    }

    File getKubectlCmd() {
        getFileProperty 'kubectl_command'
    }

    String getNodePackagesRepositoryKey() {
        getScriptProperty 'node_packages_repository_key'
    }

    String getNodePackagesRepositoryUrl() {
        getScriptProperty 'node_packages_repository_url'
    }

    File getNodePackagesRepositoryListFile() {
        getScriptFileProperty 'node_packages_repository_list_file', debian.sourcesListDir
    }

    @Override
    def getLog() {
        log
    }
}
