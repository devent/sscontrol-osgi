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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Label
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Taint
import com.anrisoftware.sscontrol.k8sbase.base.service.external.TaintFactory
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.KubeNodeClient
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.KubeNodeClientFactory
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer

import groovy.util.logging.Slf4j

/**
 * Configures the K8s service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractK8sUpstreamLinux extends ScriptBase {

    TemplateResource kubeletServiceTemplate

    TemplateResource kubeletConfigTemplate

    TemplateResource manifestsTemplate

    TemplateResource rktTemplate

    TemplateResource flannelCniTemplate

    TemplateResource clusterCmds

    TemplateResource addonsCmd

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    AddressesFactory addressesFactory

    @Inject
    TaintFactory taintFactory

    @Inject
    KubeNodeClientFactory nodeClientFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def attr = [renderers: [new UriBase64Renderer()]]
        def templates = templatesFactory.create('K8s_1_8_UpstreamSystemdTemplates', attr)
        this.kubeletServiceTemplate = templates.getResource('kubelet_service')
        this.kubeletConfigTemplate = templates.getResource('kubelet_config')
        this.manifestsTemplate = templates.getResource('manifests_template')
        this.rktTemplate = templates.getResource('rkt_template')
        this.flannelCniTemplate = templates.getResource('flannel_cni_template')
        this.clusterCmds = templates.getResource('cluster_cmds')
        this.addonsCmd = templates.getResource('addons_cmd')
    }

    def setupMiscDefaults() {
        log.debug 'Setup misc defaults for {}', service
        K8s service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug "debug", level: defaultLogLevel
        }
        if (!service.containerRuntime) {
            service.containerRuntime = defaultContainerRuntime
        }
        if (!service.allowPrivileged) {
            service.privileged defaultAllowPrivileged
        }
        if (service.tls.cert) {
            service.tls.certName = defaultKubernetesTlsCertName
        }
        if (service.tls.key) {
            service.tls.keyName = defaultKubernetesTlsKeyName
        }
        if (service.tls.ca) {
            service.tls.caName = defaultKubernetesTlsCaName
        }
    }

    def setupClusterHostDefaults() {
        K8s service = service
        ClusterHost host = service.clusterHost
        if (!host) {
            return
        }
        if (!host.proto) {
            if (host.credentials.type == 'cert') {
                host.proto = defaultApiProtocolSecure
            } else {
                host.proto = defaultApiProtocolInsecure
            }
        }
        if (!host.port) {
            if (host.credentials.type == 'cert') {
                host.port = defaultApiPortSecure
            } else {
                host.proto = defaultApiPortInsecure
            }
        }
    }

    def setupClusterDefaults() {
        log.debug 'Setup cluster defaults for {}', service
        K8s service = service
        if (!service.cluster.hostnameOverride) {
            service.cluster.hostnameOverride = advertiseAddress
        }
        if (!service.cluster.serviceRange) {
            service.cluster.serviceRange = defaultServiceNetwork
        }
        if (!service.cluster.podRange) {
            service.cluster.podRange = defaultPodNetwork
        }
        if (!service.cluster.dnsAddress) {
            service.cluster.dnsAddress = defaultDnsServiceAddress
        }
        if (service.cluster.apiServers.isEmpty()) {
            service.cluster.apiServers.addAll defaultApiServers
        }
    }

    def setupClusterApiDefaults() {
        log.debug 'Setup cluster api defaults for {}', service
        K8s service = service
        if (!service.cluster.port) {
            if (service.kubelet.tls.cert) {
                service.cluster.port = defaultApiPortSecure
            } else {
                service.cluster.port = defaultApiPortInsecure
            }
        }
        if (!service.cluster.protocol) {
            if (service.kubelet.tls.cert) {
                service.cluster.protocol = defaultApiProtocolSecure
            } else {
                service.cluster.protocol = defaultApiProtocolInsecure
            }
        }
    }

    def setupKubeletDefaults() {
        log.debug 'Setup kubelet defaults for {}', service
        K8s service = service
        if (!service.kubelet.port) {
            service.kubelet.port = defaultKubeletPort
        }
        if (service.kubelet.preferredAddressTypes.size() == 0) {
            service.kubelet.preferredAddressTypes.addAll defaultPreferredAddressTypes
        }
        if (service.kubelet.tls.ca) {
            service.kubelet.tls.caName = defaultKubeletTlsCaName
        }
        if (service.kubelet.tls.cert) {
            service.kubelet.tls.certName = defaultKubeletTlsCertName
        }
        if (service.kubelet.tls.key) {
            service.kubelet.tls.keyName = defaultKubeletTlsKeyName
        }
        if (service.kubelet.client.ca) {
            service.kubelet.client.caName = defaultKubeletClientCaName
        }
        if (service.kubelet.client.cert) {
            service.kubelet.client.certName = defaultKubeletClientCertName
        }
        if (service.kubelet.client.key) {
            service.kubelet.client.keyName = defaultKubeletClientKeyName
        }
    }

    def setupPluginsDefaults() {
        log.debug 'Setup plugins defaults for {}', service
        K8s service = service
        service.plugins.findAll {it.value.hasProperty('port')} each {
            def name = it.value.name
            if (!it.value.port) {
                it.value.port = getDefaultPluginPort(name)
            }
        }
        service.plugins.findAll {it.value.hasProperty('protocol') && it.value.hasProperty('tls')} each {
            def name = it.value.name
            if (!it.value.protocol) {
                it.value.protocol = getDefaultPluginProtocol(name, it.value.tls)
            }
        }
        service.plugins.findAll {it.value.hasProperty('tls')} each {
            def name = it.value.name
            if (!it.value.tls.caName) {
                it.value.tls.caName = getDefaultPluginCaName(name)
            }
            if (!it.value.tls.certName) {
                it.value.tls.certName = getDefaultPluginCertName(name)
            }
            if (!it.value.tls.keyName) {
                it.value.tls.keyName = getDefaultPluginKeyName(name)
            }
        }
    }

    /**
     * Set some additional kernel parameter.
     */
    def setupKernelParameter() {
        log.info 'Setup max_map_count.'
        shell privileged: true, "sysctl -w vm.max_map_count=$maxMapCount"
        replace privileged: true, dest: sysctlFile with {
            line "s/#?vm.max_map_count=\\d*/vm.max_map_count=$maxMapCount/"
            it
        } call()
    }

    def createDirectories() {
        log.info 'Create k8s directories.'
        def dirs = [
            certsDir,
        ]
        shell privileged: true, """
mkdir -p ${dirs.join(' ')}
chmod o-rx '$certsDir'
""" call()
    }

    def uploadK8sCertificates() {
        log.info 'Uploads k8s certificates.'
        def certsdir = certsDir
        K8s service = service
        uploadTlsCerts tls: service.tls, name: 'k8s-tls'
        uploadTlsCerts tls: service.kubelet.tls, name: 'kubelet-tls'
        uploadTlsCerts tls: service.kubelet.client, name: 'kubelet-client'
    }

    def uploadEtcdCertificates() {
        uploadTlsCerts tls: etcdTls, name: 'etcd'
    }

    def createKubeletConfig() {
        log.info 'Create kubelet configuration.'
        shell privileged: true, "mkdir -p $sysConfigDir" call()
        template privileged: true, resource: kubeletConfigTemplate, name: 'kubeletConfig', dest: "$sysConfigDir/kubelet", vars: [:] call()
    }

    def createWorkerKubeconfig() {
        K8s service = service
        log.info 'Create worker-kubeconfig.'
        template privileged: true, resource: manifestsTemplate, name: 'workerKubeconfig', dest: "$configDir/worker-kubeconfig.yaml", vars: [:] call()
    }

    def startKubeDnsAddon() {
        K8s service = service
        if (deployKubeDns) {
            log.info 'Start kube-dns.'
            shell resource: clusterCmds, name: 'waitApi', timeout: timeoutVeryLong call()
            shell resource: clusterCmds, name: 'startAddon', vars: [manifestFile: 'kube-dns-de.yaml'] call()
            shell resource: clusterCmds, name: 'startAddon', vars: [manifestFile: 'kube-dns-svc.yaml'] call()
            shell resource: clusterCmds, name: 'startAddon', vars: [manifestFile: 'kube-dns-autoscaler-de.yaml'] call()
        }
    }

    def startKubeDashboardAddon() {
        K8s service = service
        if (deployKubeDashboard) {
            log.info 'Start dashboard.'
            shell resource: clusterCmds, name: 'waitApi', timeout: timeoutVeryLong call()
            shell resource: clusterCmds, name: 'startAddon', vars: [manifestFile: 'kube-dashboard-de.yaml'] call()
            shell resource: clusterCmds, name: 'startAddon', vars: [manifestFile: 'kube-dashboard-svc.yaml'] call()
        }
    }

    def startCalicoAddon() {
        K8s service = service
        if (!havePluginCalico) {
            return
        }
        log.info 'Start Calico.'
        shell resource: clusterCmds, name: 'waitApi', timeout: timeoutVeryLong call()
        shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'calico.yaml'] call()
    }

    def applyTaints() {
        K8s service = service
        if (service.taints.isEmpty()) {
            log.debug 'No taints to apply, nothing to do.'
            return
        }
        if (!service.clusterHost) {
            log.debug 'No cluster host, nothing to do.'
            return
        }
        log.info 'Apply taints for {}.', service
        def node = service.cluster.name
        def nodeClient = createNodeClient()
        def vars = [:]
        vars.cluster = service.clusterHost
        vars.kubeconfigFile = createTmpFile()
        try {
            nodeClient.waitNodeReady robobeeLabelNode, service.cluster.name, timeoutLong
            kubectlCluster.uploadKubeconfig(vars)
            service.taints.each { String key, Taint taint ->
                log.info 'Apply taint {} for {}.', taint, service
                kubectlCluster.applyTaintNode vars, node, taint
            }
        } finally {
            deleteTmpFile vars.kubeconfigFile
        }
    }

    def applyLabels() {
        K8s service = service
        if (service.labels.isEmpty()) {
            log.debug 'No labels to apply, nothing to do.'
            return
        }
        if (!service.clusterHost) {
            log.debug 'No cluster host, nothing to do.'
            return
        }
        log.info 'Apply labels for {}.', service
        def node = service.cluster.name
        def nodeClient = createNodeClient()
        def vars = [:]
        vars.cluster = service.clusterHost
        vars.kubeconfigFile = createTmpFile()
        try {
            nodeClient.waitNodeReady robobeeLabelNode, node, timeoutLong
            kubectlCluster.uploadKubeconfig(vars)
            service.labels.each { String key, Label label ->
                log.info 'Apply label {} for {}.', label, service
                kubectlCluster.applyLabelNode vars, node, label
            }
        } finally {
            deleteTmpFile vars.kubeconfigFile
        }
    }

    /**
     * Returns the taint to mark a node as the master node and forbid the
     * scheduling of pods.
     */
    Taint getIsmasterNoScheduleTaint() {
        def key = properties.getProperty('taint_key_ismaster', defaultProperties)
        def effect = properties.getProperty('taint_effect_ismaster', defaultProperties)
        return taintFactory.create(key: key, value: null, effect: effect)
    }

    /**
     * Returns node labels to be set after the node was registered.
     * See <a href="https://kubernetes.io/docs/admin/kubelet/">kubelet --node-labels mapStringString</a>
     */
    List getNodeLabels() {
        K8s service = service
        def name = service.cluster.name
        def label = robobeeLabelNamespace
        def l = ["${label}/node=${name}"]
        service.labels.inject(l) { list, k, v ->
            list << "${v.key}=${v.value}"
        }
    }

    /**
     * Returns node taints to be set after the node was registered.
     * See <a href="https://kubernetes.io/docs/admin/kubelet/">kubelet --register-with-taints []api.Taint</a>
     */
    List getNodeTaints() {
        def list = []
        K8s service = service
        service.taints.each { String key, Taint taint ->
            list << taintString(taint)
        }
        return list
    }

    String taintString(Taint taint) {
        "${taint.key}=${taint.value?taint.value:''}:${taint.effect}"
    }

    /**
     * Returns the run kubectl for the cluster.
     */
    abstract AbstractKubectlLinux getKubectlCluster()

    /**
     * Returns the kubernetes node client.
     */
    KubeNodeClient createNodeClient() {
        K8s service = service
        nodeClientFactory.create service.clusterHost, this
    }

    def getDefaultLogLevel() {
        properties.getNumberProperty('default_log_level', defaultProperties).intValue()
    }

    def getDefaultContainerRuntime() {
        properties.getProperty 'default_container_runtime', defaultProperties
    }

    def getDefaultAllowPrivileged() {
        getScriptBooleanProperty "default_allow_privileged"
    }

    /**
     * Returns the default address to bind the API server for insecure
     * connections.
     */
    def getDefaultInsecureAddress() {
        properties.getProperty 'default_bind_insecure_address', defaultProperties
    }

    def getDefaultSecureAddress() {
        properties.getProperty 'default_bind_secure_address', defaultProperties
    }

    def getDefaultPort() {
        properties.getNumberProperty 'default_bind_port', defaultProperties
    }

    def getDefaultInsecurePort() {
        properties.getNumberProperty 'default_insecure_port', defaultProperties
    }

    def getDefaultPodNetwork() {
        properties.getProperty 'default_pod_network', defaultProperties
    }

    def getDefaultServiceNetwork() {
        properties.getProperty 'default_service_network', defaultProperties
    }

    def getDefaultKubernetesApiAddress() {
        properties.getProperty 'default_kubernetes_api_address', defaultProperties
    }

    def getDefaultDnsServiceAddress() {
        properties.getProperty 'default_dns_service_address', defaultProperties
    }

    List getDefaultApiServers() {
        properties.getListProperty 'default_api_servers', defaultProperties
    }

    def getDefaultKubeletPort() {
        properties.getNumberProperty 'default_kubelet_port', defaultProperties
    }

    def getDefaultClusterRange() {
        properties.getProperty 'default_cluster_range', defaultProperties
    }

    def getDefaultAdmissions() {
        properties.getListProperty 'default_admissions', defaultProperties
    }

    def getDefaultPreferredAddressTypes() {
        properties.getListProperty 'default_kubelet_preferred_address_types', defaultProperties
    }

    def getDefaultKubernetesTlsCaName() {
        properties.getProperty 'default_kubernetes_tls_ca_name', defaultProperties
    }

    def getDefaultKubernetesTlsCertName() {
        properties.getProperty 'default_kubernetes_tls_cert_name', defaultProperties
    }

    def getDefaultKubernetesTlsKeyName() {
        properties.getProperty 'default_kubernetes_tls_key_name', defaultProperties
    }

    def getDefaultAccountTlsCaName() {
        properties.getProperty 'default_account_tls_ca_name', defaultProperties
    }

    def getDefaultAccountTlsCertName() {
        properties.getProperty 'default_account_tls_cert_name', defaultProperties
    }

    def getDefaultAccountTlsKeyName() {
        properties.getProperty 'default_account_tls_key_name', defaultProperties
    }

    def getDefaultKubeletTlsCaName() {
        properties.getProperty 'default_kubelet_tls_ca_name', defaultProperties
    }

    def getDefaultKubeletTlsCertName() {
        properties.getProperty 'default_kubelet_tls_cert_name', defaultProperties
    }

    def getDefaultKubeletTlsKeyName() {
        properties.getProperty 'default_kubelet_tls_key_name', defaultProperties
    }

    def getDefaultKubeletClientCaName() {
        properties.getProperty 'default_kubelet_client_ca_name', defaultProperties
    }

    def getDefaultKubeletClientCertName() {
        properties.getProperty 'default_kubelet_client_cert_name', defaultProperties
    }

    def getDefaultKubeletClientKeyName() {
        properties.getProperty 'default_kubelet_client_key_name', defaultProperties
    }

    Map getDefaultAuthenticationTlsCaName() {
        def s = properties.getProperty 'default_authentication_tls_ca_name', defaultProperties
        Eval.me s
    }

    Map getDefaultAuthenticationTlsCertName() {
        def s = properties.getProperty 'default_authentication_tls_cert_name', defaultProperties
        Eval.me s
    }

    Map getDefaultAuthenticationTlsKeyName() {
        def s = properties.getProperty 'default_authentication_tls_key_name', defaultProperties
        Eval.me s
    }

    int getDefaultApiPortInsecure() {
        properties.getNumberProperty "default_api_port_insecure", defaultProperties
    }

    String getDefaultApiProtocolInsecure() {
        properties.getProperty "default_api_protocol_insecure", defaultProperties
    }

    int getDefaultApiPortSecure() {
        properties.getNumberProperty "default_api_port_secure", defaultProperties
    }

    String getDefaultApiProtocolSecure() {
        properties.getProperty "default_api_protocol_secure", defaultProperties
    }

    int getDefaultPluginPort(String name) {
        name = name.toLowerCase()
        properties.getNumberProperty "default_plugin_port_$name", defaultProperties
    }

    String getDefaultPluginProtocol(String name, Tls tls) {
        name = name.toLowerCase()
        def insecure = properties.getProperty "default_plugin_protocol_insecure_$name", defaultProperties
        def secure = properties.getProperty "default_plugin_protocol_secure_$name", defaultProperties
        tls.cert ? secure : insecure
    }

    String getDefaultPluginCaName(String name) {
        name = name.toLowerCase()
        properties.getProperty "default_plugin_ca_name_$name", defaultProperties
    }

    String getDefaultPluginCertName(String name) {
        name = name.toLowerCase()
        properties.getProperty "default_plugin_cert_name_$name", defaultProperties
    }

    String getDefaultPluginKeyName(String name) {
        name = name.toLowerCase()
        properties.getProperty "default_plugin_key_name_$name", defaultProperties
    }

    def getKubernetesVersion() {
        properties.getProperty 'kubernetes_version', defaultProperties
    }

    def getHypercubeImageRepo() {
        properties.getProperty 'hypercube_image_repo', defaultProperties
    }

    def getCalicoNodeVersion() {
        properties.getProperty 'calico_node_version', defaultProperties
    }

    def getCalicoNodeImageRepo() {
        properties.getProperty 'calico_node_image_repo', defaultProperties
    }

    def getCalicoCniVersion() {
        properties.getProperty 'calico_cni_version', defaultProperties
    }

    def getCalicoCniImageRepo() {
        properties.getProperty 'calico_cni_image_repo', defaultProperties
    }

    def getCalicoKubePolicyControllerVersion() {
        properties.getProperty 'calico_kube_policy_controller_version', defaultProperties
    }

    def getCalicoKubePolicyControllerImageRepo() {
        properties.getProperty 'calico_kube_policy_controller_image_repo', defaultProperties
    }

    File getKubeletUuidFile() {
        properties.getFileProperty 'kubelet_uuid_file', base, defaultProperties
    }

    Map getPluginsTargets() {
        pluginTargetsMapFactory.create service, scriptsRepository, service.plugins
    }

    boolean getHavePluginCalico() {
        K8s service = service
        service.plugins.containsKey('calico')
    }

    Tls getPluginEtcdTls() {
        K8s service = service
        if (service.plugins.containsKey('etcd')) {
            return service.plugins.etcd.tls
        } else {
            return null
        }
    }

    List getMasterHosts() {
        K8s service = service
        addressesFactory.create(service.cluster, service.cluster.apiServers).hosts
    }

    String getAdvertiseAddress() {
        K8s service = service
        if (service.cluster.advertiseAddress instanceof SshHost) {
            service.cluster.advertiseAddress.hostAddress
        } else {
            return service.cluster.advertiseAddress
        }
    }

    File getContainersLogDir() {
        properties.getFileProperty "containers_log_dir", base, defaultProperties
    }

    Tls getEtcdTls() {
        def plugin = service.plugins['etcd']
        if (plugin) {
            return plugin.tls
        } else {
            return null
        }
    }

    String getRobobeeLabelNode() {
        getScriptProperty 'robobee_label_node'
    }

    String getRobobeeLabelNamespace() {
        getScriptProperty 'robobee_label_namespace'
    }

    long getMaxMapCount() {
        getScriptNumberProperty 'max_map_count'
    }

    File getSysctlFile() {
        getScriptFileProperty 'sysctl_file'
    }

    Duration getKubectlTimeout() {
        getScriptDurationProperty 'kubectl_timeout'
    }

    boolean getFailSwapOn() {
        getScriptBooleanProperty 'fail_swap_on'
    }

    Map getFeatureGates() {
        getScriptMapProperty 'feature_gates'
    }

    @Override
    def getLog() {
        log
    }
}
