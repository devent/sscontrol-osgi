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

    TemplateResource servicesTemplate

    TemplateResource configsTemplate

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('K8sMaster_1_5_Upstream_Systemd_Templates')
        this.servicesTemplate = templates.getResource('k8s_master_services')
        this.configsTemplate = templates.getResource('k8s_master_configs')
    }

    def setupDefaults() {
        K8sMaster service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug "debug", level: defaultLogLevel
        }
        if (!service.allowPrivileged) {
            service.privileged defaultAllowPrivileged
        }
        if (!service.binding.insecureAddress) {
            service.binding.insecureAddress = defaultInsecureAddress
        }
        if (!service.binding.secureAddress) {
            service.binding.secureAddress = defaultSecureAddress
        }
        if (!service.binding.port) {
            service.binding.port = defaultPort
        }
        if (!service.kubelet.binding.port) {
            service.kubelet.binding.port = defaultKubeletPort
        }
        if (!service.cluster.range) {
            service.cluster.range = defaultClusterRange
        }
        if (service.admissions.size() == 0) {
            service.admissions.addAll defaultAdmissions
        }
        if (service.kubelet.preferredAddressTypes.size() == 0) {
            service.kubelet.preferredAddressTypes.addAll defaultPreferredAddressTypes
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
        if (!service.kubelet.tls.caName) {
            service.kubelet.tls.caName = defaultKubeletTlsCaName
        }
        if (!service.kubelet.tls.certName) {
            service.kubelet.tls.certName = defaultKubeletTlsCertName
        }
        if (!service.kubelet.tls.keyName) {
            service.kubelet.tls.keyName = defaultKubeletTlsKeyName
        }
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
    }

    def createDirectories() {
        log.info 'Create k8s-master directories.'
        def dir = systemdSystemDir
        def tmpdir = systemdTmpfilesDir
        def rundir = runDir
        def certsdir = certsDir
        shell privileged: true, """
mkdir -p '$dir'
mkdir -p '$tmpdir'
mkdir -p '$rundir'
useradd -r $user
chown $user '$rundir'
mkdir -p '$certsdir'
chown ${user}.root '$certsdir'
chmod o-rx '$certsdir'
""" call()
    }

    def uploadCertificates() {
        log.info 'Uploads k8s-master certificates.'
        def certsdir = certsDir
        K8sMaster service = service
        [
            [
                name: 'service.tls.ca',
                src: service.tls.ca,
                dest: "$certsdir/$service.tls.caName",
            ],
            [
                name: 'service.tls.cert',
                src: service.tls.cert,
                dest: "$certsdir/$service.tls.certName",
            ],
            [
                name: 'service.tls.key',
                src: service.tls.key,
                dest: "$certsdir/$service.tls.keyName",
            ],
            [
                name: 'service.kubelet.tls.ca',
                src: service.kubelet.tls.ca,
                dest: "$certsdir/$service.kubelet.tls.caName",
            ],
            [
                name: 'service.kubelet.tls.cert',
                src: service.kubelet.tls.cert,
                dest: "$certsdir/$service.kubelet.tls.certName",
            ],
            [
                name: 'service.kubelet.tls.key',
                src: service.kubelet.tls.key,
                dest: "$certsdir/$service.kubelet.tls.keyName",
            ],
        ].each {
            if (it.src) {
                copyResource it call()
            }
        }
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
            [
                [
                    name: 'tls.ca',
                    src: tls.ca,
                    dest: "$certsdir/$tls.caName",
                ],
                [
                    name: 'tls.cert',
                    src: tls.cert,
                    dest: "$certsdir/$tls.certName",
                ],
                [
                    name: 'tls.key',
                    src: tls.key,
                    dest: "$certsdir/$tls.keyName",
                ],
            ].each {
                if (it.src) {
                    copyResource it call()
                }
            }
        }
    }

    def createServices() {
        log.info 'Create k8s-master services.'
        def dir = systemdSystemDir
        def tmpdir = systemdTmpfilesDir
        [
            [
                resource: servicesTemplate,
                name: 'kubeApiserverService',
                privileged: true,
                dest: "$dir/kube-apiserver.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'kubeControllerManagerService',
                privileged: true,
                dest: "$dir/kube-controller-manager.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'kubeSchedulerService',
                privileged: true,
                dest: "$dir/kube-scheduler.service",
                vars: [:],
            ],
            [
                resource: servicesTemplate,
                name: 'servicesKubernetesConf',
                privileged: true,
                dest: "$tmpdir/kubernetes.conf",
                vars: [:],
            ],
        ].each { template it call() }
        shell privileged: true, "systemctl daemon-reload" call()
    }

    def createConfig() {
        log.info 'Create k8s-master configuration.'
        def dir = configDir
        shell privileged: true, "mkdir -p $dir" call()
        [
            [
                resource: configsTemplate,
                name: 'kubeConfig',
                privileged: true,
                dest: "$configDir/config",
                vars: [:],
            ],
            [
                resource: configsTemplate,
                name: 'kubeApiserverConfig',
                privileged: true,
                dest: "$configDir/apiserver",
                vars: [:],
            ],
            [
                resource: configsTemplate,
                name: 'kubeControllerManagerConfig',
                privileged: true,
                dest: "$configDir/controller-manager",
                vars: [:],
            ],
            [
                resource: configsTemplate,
                name: 'kubeSchedulerConfig',
                privileged: true,
                dest: "$configDir/scheduler",
                vars: [:],
            ],
        ].each { template it call() }
    }

    File getSystemdSystemDir() {
        properties.getFileProperty "systemd_system_dir", base, defaultProperties
    }

    File getSystemdTmpfilesDir() {
        properties.getFileProperty "systemd_tmpfiles_dir", base, defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    File getRunDir() {
        properties.getFileProperty "run_dir", base, defaultProperties
    }

    File getCertsDir() {
        properties.getFileProperty "certs_dir", base, defaultProperties
    }

    String getUser() {
        properties.getProperty "user", defaultProperties
    }

    def getDefaultLogLevel() {
        properties.getNumberProperty('default_log_level', defaultProperties).intValue()
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

    Map getPluginsTargets() {
        pluginTargetsMapFactory.create service, scriptsRepository, service.plugins
    }

    @Override
    def getLog() {
        log
    }
}
