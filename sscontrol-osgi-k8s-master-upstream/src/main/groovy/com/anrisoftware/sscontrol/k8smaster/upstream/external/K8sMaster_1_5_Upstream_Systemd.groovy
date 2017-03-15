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

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8sMaster_1_5_Upstream_Systemd_Templates')
        this.kubeletServiceTemplate = templates.getResource('kubelet_service')
        this.kubeletConfigTemplate = templates.getResource('kubelet_config')
        this.manifestsTemplate = templates.getResource('manifests_template')
        this.rktTemplate = templates.getResource('rkt_template')
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
        if (!service.tls.caName) {
            service.tls.caName = defaultKubernetesTlsCaName
        }
        if (!service.tls.certName) {
            service.tls.certName = defaultKubernetesTlsCertName
        }
        if (!service.tls.keyName) {
            service.tls.keyName = defaultKubernetesTlsKeyName
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
        if (!service.kubelet.tls.caName) {
            service.kubelet.tls.caName = defaultKubeletTlsCaName
        }
        if (!service.kubelet.tls.certName) {
            service.kubelet.tls.certName = defaultKubeletTlsCertName
        }
        if (!service.kubelet.tls.keyName) {
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
        service.plugins.findAll {it.value.hasProperty('protocol')} each {
            def name = it.value.name
            if (!it.value.protocol) {
                it.value.protocol = getDefaultPluginProtocol(name)
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
        uploadTlsCerts tls: service.tls, name: 'kubelet-tls'
    }

    def uploadAuthenticationsCertificates() {
        K8sMaster service = service
        service.authentications.findAll { it.tls  } each {
            Tls tls = it.tls
            uploadTlsCerts tls: tls, name: it.toString()
        }
    }

    def uploadEtcdCertificates() {
        uploadTlsCerts tls: etcdTls
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
        def dir = manifestsDir
        def srv = srvManifestsDir
        shell privileged: true, "mkdir -p $dir; mkdir -p $srv" call()
        [
            [
                resource: manifestsTemplate,
                name: 'kubeProxyManifest',
                privileged: true,
                dest: "$dir/kube-proxy.yml",
                vars: [:],
            ],
            [
                resource: manifestsTemplate,
                name: 'kubeApiserverManifest',
                privileged: true,
                dest: "$dir/kube-apiserver.yml",
                vars: [:],
            ],
            [
                resource: manifestsTemplate,
                name: 'kubeControllerManagerManifest',
                privileged: true,
                dest: "$dir/kube-controller-manager.yml",
                vars: [:],
            ],
        ].each { template it call() }
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

    String getDefaultPluginProtocol(String name) {
        name = name.toLowerCase()
        properties.getProperty "default_plugin_protocol_$name", defaultProperties
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
