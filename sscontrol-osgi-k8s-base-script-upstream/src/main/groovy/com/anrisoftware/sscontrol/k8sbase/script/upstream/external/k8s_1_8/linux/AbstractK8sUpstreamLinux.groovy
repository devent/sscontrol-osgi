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
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
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

    TemplateResource kubeadmConfigTemplate

    TemplateResource kubeletConfigTemplate

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    AddressesFactory addressesFactory

    @Inject
    TaintFactory taintFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def attr = [renderers: [new UriBase64Renderer()]]
        def templates = templatesFactory.create('K8s_1_8_UpstreamTemplates', attr)
        this.kubeadmConfigTemplate = templates.getResource('kubeadm_config')
        this.kubeletConfigTemplate = templates.getResource('kubelet_config')
    }

    def setupMiscDefaults() {
        log.debug 'Setup misc defaults for {}', service
        K8s service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug "debug", level: defaultLogLevel
        }
        if (!service.containerRuntime) {
            service.containerRuntime = scriptProperties.default_container_runtime
        }
        if (!service.allowPrivileged) {
            service.privileged scriptBooleanProperties.default_allow_privileged
        }
        if (service.tls.cert) {
            service.tls.certName = scriptProperties.default_kubernetes_tls_cert_name
        }
        if (service.tls.key) {
            service.tls.keyName = scriptProperties.default_kubernetes_tls_key_name
        }
        if (service.tls.ca) {
            service.tls.caName = scriptProperties.default_kubernetes_tls_ca_name
        }
        if (service.ca.cert) {
            service.ca.certName = scriptProperties.default_kubernetes_ca_cert_name
        }
        if (service.ca.key) {
            service.ca.keyName = scriptProperties.default_kubernetes_ca_key_name
        }
    }

    def setupLabelsDefaults() {
        log.debug 'Setup default labels for {}', service
        K8s service = service
        def name = service.cluster.name
        def label = robobeeLabelNode
        service.label key: label, value: name
    }

    def setupClusterHostDefaults() {
        K8s service = service
        ClusterHost host = service.clusterHost
        if (!host) {
            return
        }
        if (!host.proto) {
            if (host.credentials.type == 'cert') {
                host.proto = scriptProperties.default_api_protocol_secure
            } else {
                host.proto = scriptProperties.default_api_protocol_insecure
            }
        }
        if (!host.port) {
            if (host.credentials.type == 'cert') {
                host.port = scriptNumberProperties.default_api_port_secure
            } else {
                host.port = scriptNumberProperties.default_api_port_insecure
            }
        }
    }

    def setupClusterDefaults() {
        log.debug 'Setup cluster defaults for {}', service
        K8s service = service
        if (!service.cluster.serviceRange) {
            service.cluster.serviceRange = scriptProperties.default_service_network
        }
        if (!service.cluster.podRange) {
            service.cluster.podRange = scriptProperties.default_pod_network
        }
        if (!service.cluster.dnsDomain) {
            service.cluster.dnsDomain = scriptProperties.default_dns_domain
        }
        if (service.cluster.apiServers.isEmpty()) {
            service.cluster.apiServers.addAll defaultApiServers
        }
    }

    def setupClusterApiDefaults() {
        log.debug 'Setup cluster api defaults for {}', service
        K8s service = service
        if (!service.cluster.port) {
            if (service.tls.cert) {
                service.cluster.port = scriptNumberProperties.default_api_port_secure
            } else {
                service.cluster.port = scriptNumberProperties.default_api_port_insecure
            }
            if (service.ca.cert) {
                service.cluster.port = scriptNumberProperties.default_api_port_secure
            } else {
                service.cluster.port = scriptNumberProperties.default_api_port_insecure
            }
        }
        if (!service.cluster.protocol) {
            if (service.tls.cert) {
                service.cluster.protocol = scriptProperties.default_api_protocol_secure
            } else {
                service.cluster.protocol = scriptProperties.default_api_protocol_insecure
            }
            if (service.ca.cert) {
                service.cluster.protocol = scriptProperties.default_api_protocol_secure
            } else {
                service.cluster.protocol = scriptProperties.default_api_protocol_insecure
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
            service.kubelet.tls.caName = scriptProperties.default_kubelet_tls_ca_name
        }
        if (service.kubelet.tls.cert) {
            service.kubelet.tls.certName = scriptProperties.default_kubelet_tls_cert_name
        }
        if (service.kubelet.tls.key) {
            service.kubelet.tls.keyName = scriptProperties.default_kubelet_tls_key_name
        }
        if (service.kubelet.client.ca) {
            service.kubelet.client.caName = scriptProperties.default_kubelet_client_ca_name
        }
        if (service.kubelet.client.cert) {
            service.kubelet.client.certName = scriptProperties.default_kubelet_client_cert_name
        }
        if (service.kubelet.client.key) {
            service.kubelet.client.keyName = scriptProperties.default_kubelet_client_key_name
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
        log.info 'Uploads k8s certificates: {}', service.tls
        def certsdir = certsDir
        K8s service = service
        uploadTlsCerts tls: service.tls, name: 'k8s-tls'
        uploadTlsCerts tls: service.ca, name: 'k8s-ca'
    }

    def uploadEtcdCertificates() {
        log.info 'Uploads etcd certificates: {}', etcdTls
        uploadTlsCerts tls: etcdTls, name: 'etcd'
    }

    def createKubeadmConfig() {
        log.info 'Create kubeadm configuration.'
        template privileged: true, resource: kubeadmConfigTemplate,
        name: 'kubeadmConfig', dest: "/root/kubeadm.yaml", vars: [:] call()
    }

    def createKubeletConfig() {
        log.info 'Create kubelet configuration.'
        template privileged: true, resource: kubeletConfigTemplate,
        name: 'kubeletConfig', dest: scriptProperties.kubelet_extra_conf, vars: [:] call()
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
        def nodeClient = kubectlCluster
        def vars = [:]
        vars.cluster = service.clusterHost
        vars.kubeconfigFile = getKubeconfigFile(service.clusterHost)
        try {
            nodeClient.waitNodeAvailable node, timeoutLong
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
        def key = getScriptProperty 'taint_key_ismaster'
        def effect = getScriptProperty 'taint_effect_ismaster'
        return taintFactory.create(key: key, value: null, effect: effect)
    }

    /**
     * Returns if there are node labels to be set after the node was registered.
     * See <a href="https://kubernetes.io/docs/admin/kubelet/">kubelet --node-labels mapStringString</a>
     */
    boolean getHaveLabels() {
        service.labels.size() > 0
    }

    /**
     * Returns node labels to be set after the node was registered.
     * See <a href="https://kubernetes.io/docs/admin/kubelet/">kubelet --node-labels mapStringString</a>
     */
    List getNodeLabels() {
        K8s service = service
        service.labels.inject([]) { list, k, v ->
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

    File getKubeconfigFile(ClusterHost cluster) {
        setupHostCredentials cluster
        Credentials c = cluster.credentials
        new File("/home/robobee/.kube/config")
    }

    /**
     * Setups the credentials.
     */
    def setupHostCredentials(ClusterHost host) {
        Credentials c = host.credentials
        if (!host.proto) {
            if (c.hasProperty('tls') && c.tls.ca) {
                host.proto = scriptProperties.default_server_proto_secured
            } else {
                host.proto = scriptProperties.default_server_proto_unsecured
            }
        }
        if (!host.port) {
            if (c.hasProperty('tls') && c.tls.ca) {
                host.port = scriptNumberProperties.default_server_port_secured
            } else {
                host.port = scriptNumberProperties.default_server_port_unsecured
            }
        }
        if (c.hasProperty('tls')) {
            Tls tls = c.tls
            if (tls.cert) {
                tls.certName = scriptProperties.default_credentials_tls_cert_name
            }
            if (tls.key) {
                tls.keyName = scriptProperties.default_credentials_tls_key_name
            }
            if (tls.ca) {
                tls.caName = scriptProperties.default_credentials_tls_ca_name
            }
        }
    }

    def getDefaultContainerRuntime() {
        getScriptProperty 'default_container_runtime'
    }

    def getDefaultAllowPrivileged() {
        getScriptBooleanProperty "default_allow_privileged"
    }

    List getDefaultApiServers() {
        properties.getListProperty 'default_api_servers', defaultProperties
    }

    def getDefaultKubeletPort() {
        properties.getNumberProperty 'default_kubelet_port', defaultProperties
    }

    def getDefaultClusterRange() {
        getScriptProperty 'default_cluster_range'
    }

    def getDefaultAdmissions() {
        properties.getListProperty 'default_admissions', defaultProperties
    }

    def getDefaultPreferredAddressTypes() {
        properties.getListProperty 'default_kubelet_preferred_address_types', defaultProperties
    }

    Map getDefaultAuthenticationTlsCaName() {
        def s = getScriptProperty 'default_authentication_tls_ca_name'
        Eval.me s
    }

    Map getDefaultAuthenticationTlsCertName() {
        def s = getScriptProperty 'default_authentication_tls_cert_name'
        Eval.me s
    }

    Map getDefaultAuthenticationTlsKeyName() {
        def s = getScriptProperty 'default_authentication_tls_key_name'
        Eval.me s
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
        getScriptProperty 'kubernetes_version'
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
