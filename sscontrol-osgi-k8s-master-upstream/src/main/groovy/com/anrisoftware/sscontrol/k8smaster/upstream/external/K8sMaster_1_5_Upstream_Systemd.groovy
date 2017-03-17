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
package com.anrisoftware.sscontrol.k8smaster.upstream.external

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster
import com.anrisoftware.sscontrol.k8smaster.upstream.external.PluginTargetsMap.PluginTargetsMapFactory
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master 1.5 service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sMaster_1_5_Upstream_Systemd extends ScriptBase {

    TemplateResource kubeletServiceTemplate

    TemplateResource kubeletConfigTemplate

    TemplateResource manifestsTemplate

    TemplateResource rktTemplate

    TemplateResource flannelCniTemplate

    TemplateResource addonsCmd

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8sMaster_1_5_Upstream_Systemd_Templates')
        this.kubeletServiceTemplate = templates.getResource('kubelet_service')
        this.kubeletConfigTemplate = templates.getResource('kubelet_config')
        this.manifestsTemplate = templates.getResource('manifests_template')
        this.rktTemplate = templates.getResource('rkt_template')
        this.flannelCniTemplate = templates.getResource('flannel_cni_template')
        this.addonsCmd = templates.getResource('addons_cmd')
    }

    def setupMiscDefaults() {
        log.debug 'Setup misc defaults for {}', service
        K8sMaster service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug "debug", level: defaultLogLevel
        }
        if (!service.containerRuntime) {
            service.containerRuntime = defaultContainerRuntime
        }
        if (!service.allowPrivileged) {
            service.privileged defaultAllowPrivileged
        }
        if (service.admissions.size() == 0) {
            service.admissions.addAll defaultAdmissions
        }
        if (!service.account.tls.ca) {
            service.account.tls.ca = service.tls.ca
        }
        if (!service.account.tls.key) {
            service.account.tls.key = service.tls.key
        }
        if (service.account.tls.ca) {
            service.account.tls.caName = defaultAccountTlsCaName
        }
        if (service.account.tls.cert) {
            service.account.tls.certName = defaultAccountTlsCertName
        }
        if (service.account.tls.key) {
            service.account.tls.keyName = defaultAccountTlsKeyName
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

    def setupClusterDefaults() {
        log.debug 'Setup cluster defaults for {}', service
        K8sMaster service = service
        assertThat("cluster advertise address=null", service.cluster.advertiseAddress, not(isEmptyOrNullString()))
        if (!service.cluster.hostnameOverride) {
            service.cluster.hostnameOverride = service.cluster.advertiseAddress
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

    def setupBindDefaults() {
        log.debug 'Setup bind defaults for {}', service
        K8sMaster service = service
        if (!service.binding.insecureAddress) {
            service.binding.insecureAddress = defaultInsecureAddress
        }
        if (!service.binding.secureAddress) {
            service.binding.secureAddress = defaultSecureAddress
        }
        if (!service.binding.port) {
            service.binding.port = defaultPort
        }
        if (!service.binding.insecurePort) {
            service.binding.insecurePort = defaultInsecurePort
        }
    }

    def setupKubeletDefaults() {
        log.debug 'Setup kubelet defaults for {}', service
        K8sMaster service = service
        if (!service.kubelet.binding.port) {
            service.kubelet.binding.port = defaultKubeletPort
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
    }

    def setupAuthenticationsDefaults() {
        log.debug 'Setup authentications defaults for {}', service
        K8sMaster service = service
        service.authentications.findAll { it.tls  } each {
            Tls tls = it.tls
            if (!tls.caName) {
                tls.caName = defaultAuthenticationTlsCaName[it.type]
            }
            if (!tls.certName) {
                tls.certName = defaultAuthenticationTlsCertName[it.type]
            }
            if (!tls.keyName) {
                tls.keyName = defaultAuthenticationTlsKeyName[it.type]
            }
        }
    }

    def setupPluginsDefaults() {
        log.debug 'Setup plugins defaults for {}', service
        K8sMaster service = service
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

    def createDirectories() {
        log.info 'Create k8s-master directories.'
        def dirs = [
            configDir,
            certsDir,
            cniNetDir,
            systemdSystemDir
        ]
        shell privileged: true, """
mkdir -p ${dirs.join(' ')}
chmod o-rx '$certsDir'
""" call()
    }

    def uploadK8sCertificates() {
        log.info 'Uploads k8s-master certificates.'
        def certsdir = certsDir
        K8sMaster service = service
        uploadTlsCerts tls: service.tls, name: 'k8s-tls'
        uploadTlsCerts tls: service.account.tls, name: 'account-tls'
        uploadTlsCerts tls: service.kubelet.tls, name: 'kubelet-tls'
    }

    def uploadAuthenticationsCertificates() {
        K8sMaster service = service
        service.authentications.findAll { it.tls  } each {
            Tls tls = it.tls
            uploadTlsCerts tls: tls, name: it.toString()
        }
    }

    def uploadEtcdCertificates() {
        uploadTlsCerts tls: etcdTls, name: 'etcd'
    }

    def createKubeletService() {
        log.info 'Create kubelet service.'
        template resource: kubeletServiceTemplate,
        name: 'kubeletService',
        privileged: true,
        dest: "$systemdSystemDir/kubelet.service",
        vars: [:] call()
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def createKubeletConfig() {
        log.info 'Create kubelet configuration.'
        shell privileged: true, "mkdir -p $sysConfigDir" call()
        template resource: kubeletConfigTemplate,
        name: 'kubeletConfig',
        privileged: true,
        dest: "$sysConfigDir/kubelet",
        vars: [:] call()
    }

    def createRkt() {
        log.info 'Create host-rkt.'
        def dir = binDir
        shell privileged: true, "mkdir -p $dir" call()
        template resource: rktTemplate,
        name: 'hostRkt',
        privileged: true,
        dest: "$dir/host-rkt",
        vars: [:] call()
        shell privileged: true, "chmod +x $dir/host-rkt" call()
    }

    def createKubeletManifests() {
        log.info 'Create kubelet manifests files.'
        K8sMaster service = service
        def dir = manifestsDir
        def srv = srvManifestsDir
        shell privileged: true, "mkdir -p $dir; mkdir -p $srv" call()
        def templates = [
            [
                resource: manifestsTemplate,
                name: 'kubeProxyManifest',
                privileged: true,
                dest: "$dir/kube-proxy.yaml",
                vars: [:],
            ],
            [
                resource: manifestsTemplate,
                name: 'kubeApiserverManifest',
                privileged: true,
                dest: "$dir/kube-apiserver.yaml",
                vars: [:],
            ],
            [
                resource: manifestsTemplate,
                name: 'kubeControllerManagerManifest',
                privileged: true,
                dest: "$dir/kube-controller-manager.yaml",
                vars: [:],
            ],
            [
                resource: manifestsTemplate,
                name: 'kubeSchedulerManifest',
                privileged: true,
                dest: "$dir/kube-scheduler.yaml",
                vars: [:],
            ],
        ]
        if (deployKubeDns) {
            templates << [
                resource: manifestsTemplate,
                name: 'kubeDnsDeManifest',
                privileged: true,
                dest: "$srv/kube-dns-de.yaml",
                vars: [:],
            ]
            templates << [
                resource: manifestsTemplate,
                name: 'kubeDnsAutoscalerDeManifest',
                privileged: true,
                dest: "$srv/kube-dns-autoscaler-de.yaml",
                vars: [:],
            ]
            templates << [
                resource: manifestsTemplate,
                name: 'kubeDnsSvcManifest',
                privileged: true,
                dest: "$srv/kube-dns-svc.yaml",
                vars: [:],
            ]
        }
        if (deployHeapster) {
            templates << [
                resource: manifestsTemplate,
                name: 'heapsterDeManifest',
                privileged: true,
                dest: "$srv/heapster-de.yaml",
                vars: [:],
            ]
            templates << [
                resource: manifestsTemplate,
                name: 'heapsterSvcManifest',
                privileged: true,
                dest: "$srv/heapster-svc.yaml",
                vars: [:],
            ]
        }
        if (deployKubeDashboard) {
            templates << [
                resource: manifestsTemplate,
                name: 'kubeDashboardDeManifest',
                privileged: true,
                dest: "$srv/kube-dashboard-de.yaml",
                vars: [:],
            ]
            templates << [
                resource: manifestsTemplate,
                name: 'kubeDashboardSvcManifest',
                privileged: true,
                dest: "$srv/kube-dashboard-svc.yaml",
                vars: [:],
            ]
        }
        if (deployCalico && service.plugins.containsKey('calico')) {
            templates << [
                resource: manifestsTemplate,
                name: 'calicoManifest',
                privileged: true,
                dest: "$srv/calico.yaml",
                vars: [:],
            ]
        }
        templates.each { template it call() }
    }

    def createFlannelCni() {
        K8sMaster service = service
        if (!service.plugins.containsKey('flannel')) {
            return
        }
        log.info 'Create flannel cni drop-in.'
        def dir = cniNetDir
        shell privileged: true, "mkdir -p $dir" call()
        template resource: flannelCniTemplate,
        name: 'flannelCniDropin',
        privileged: true,
        dest: "$dir/10-flannel.conf",
        vars: [:] call()
    }

    def stopServices() {
        stopSystemdService 'kubelet'
    }

    def startServices() {
        startEnableSystemdService 'kubelet'
    }

    def startAddons() {
        K8sMaster service = service
        if (deployKubeDns) {
            log.info 'Start kube-dns.'
            shell resource: addonsCmd, name: 'waitApi' call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-de.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-svc.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-autoscaler-de.yaml'] call()
        }
        if (deployHeapster) {
            log.info 'Start heapster.'
            shell resource: addonsCmd, name: 'waitApi' call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'heapster-de.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'heapster-svc.yaml'] call()
        }
        if (deployKubeDashboard) {
            log.info 'Start heapster.'
            shell resource: addonsCmd, name: 'waitApi' call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dashboard-de.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dashboard-svc.yaml'] call()
        }
    }

    def startCalico() {
        K8sMaster service = service
        if (!service.plugins.containsKey('calico')) {
            return
        }
        log.info 'Start Calico.'
        shell resource: addonsCmd, name: 'waitApi' call()
        shell privileged: true, resource: addonsCmd, name: 'startCalico' call()
    }

    def getDefaultLogLevel() {
        properties.getNumberProperty('default_log_level', defaultProperties).intValue()
    }

    def getDefaultContainerRuntime() {
        properties.getProperty 'default_container_runtime', defaultProperties
    }

    def getDefaultAllowPrivileged() {
        properties.getBooleanProperty 'default_allow_privileged', defaultProperties
    }

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

    File getKubeletUuidFile() {
        properties.getFileProperty 'kubelet_uuid_file', base, defaultProperties
    }

    Map getPluginsTargets() {
        pluginTargetsMapFactory.create service, scriptsRepository, service.plugins
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    File getManifestsDir() {
        properties.getFileProperty "manifests_dir", base, defaultProperties
    }

    File getSrvManifestsDir() {
        properties.getFileProperty "srv_manifests_dir", base, defaultProperties
    }

    File getCniNetDir() {
        properties.getFileProperty "cni_net_dir", base, defaultProperties
    }

    File getCniBinDir() {
        properties.getFileProperty "cni_bin_dir", base, defaultProperties
    }

    File getContainersLogDir() {
        properties.getFileProperty "containers_log_dir", base, defaultProperties
    }

    boolean getDeployKubeDns() {
        properties.getBooleanProperty "deploy_kube_dns", defaultProperties
    }

    boolean getDeployHeapster() {
        properties.getBooleanProperty "deploy_heapster", defaultProperties
    }

    boolean getDeployKubeDashboard() {
        properties.getBooleanProperty "deploy_kube_dashboard", defaultProperties
    }

    boolean getDeployCalico() {
        properties.getBooleanProperty "deploy_calico", defaultProperties
    }

    Tls getEtcdTls() {
        def plugin = service.plugins['etcd']
        if (plugin) {
            return plugin.tls
        } else {
            return null
        }
    }

    @Override
    def getLog() {
        log
    }
}
