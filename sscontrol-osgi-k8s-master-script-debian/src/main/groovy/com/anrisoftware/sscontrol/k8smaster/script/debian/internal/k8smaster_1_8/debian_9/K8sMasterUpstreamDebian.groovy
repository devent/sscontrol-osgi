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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import static org.apache.commons.lang3.StringUtils.*
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8sbase.base.service.external.CanalPlugin
import com.anrisoftware.sscontrol.k8sbase.script.upstream.debian.external.k8s_1_8.debian.debian_9.AbstractK8sUpstreamDebian
import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.AbstractKubectlLinux
import com.anrisoftware.sscontrol.k8smaster.service.external.K8sMaster

import groovy.util.logging.Slf4j


/**
 * Configures the K8s-Master service from the upstream sources Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMasterUpstreamDebian extends AbstractK8sUpstreamDebian {

    @Inject
    K8sMasterDebianProperties debianPropertiesProvider

    KubectlClusterDebian kubectlClusterLinux

    @Override
    Object run() {
    }

    def setupDefaults() {
        setupMiscDefaults()
        setupLabelsDefaults()
        setupApiServersDefaults()
        setupClusterDefaults()
        setupClusterHostDefaults()
        setupClusterApiDefaults()
        setupBindDefaults()
        setupKubeletDefaults()
        setupPluginsDefaults()
        setupKernelParameter()
    }

    def createService() {
        createDirectories()
        uploadK8sCertificates()
        uploadEtcdCertificates()
        createKubeadmConfig()
        createKubeletConfig()
        restartKubelet()
    }

    def setupClusterDefaults() {
        K8sMaster service = service
        super.setupClusterDefaults()
        if (!service.cluster.name) {
            service.cluster.name = 'master'
        }
    }

    def postInstall() {
        applyLabels()
        applyTaints()
    }

    def setupMiscDefaults() {
        super.setupMiscDefaults()
        K8sMaster service = service
        if (service.ca.cert) {
            service.ca.certName = scriptProperties.default_kubernetes_ca_cert_name
        }
        if (service.ca.key) {
            service.ca.keyName = scriptProperties.default_kubernetes_ca_key_name
        }
    }

    def setupClusterApiDefaults() {
        log.debug 'Setup cluster api defaults for {}', service
        K8sMaster service = service
        if (!service.cluster.port) {
            service.cluster.port = scriptNumberProperties.default_api_port_secure
        }
        if (!service.cluster.protocol) {
            service.cluster.protocol = scriptProperties.default_api_protocol_secure
        }
    }

    def setupBindDefaults() {
        log.debug 'Setup bind defaults for {}', service
        K8sMaster service = service
        if (!service.binding.insecureAddress) {
            service.binding.insecureAddress = scriptProperties.default_bind_insecure_address
        }
        if (!service.binding.secureAddress) {
            service.binding.secureAddress = scriptProperties.default_bind_secure_address
        }
        if (!service.binding.port) {
            service.binding.port = scriptNumberProperties.default_bind_port
        }
        if (!service.binding.insecurePort) {
            service.binding.insecurePort = scriptNumberProperties.default_bind_insecure_port
        }
    }

    /**
     * Installs a pod network.
     */
    def installNetwork() {
        log.debug 'Installs pod network for {}', service
        K8sMaster service = service
        if (service.plugins.containsKey("canal")) {
            installCanalNetwork()
        }
    }

    def installCanalNetwork() {
        log.debug 'Installs canal as the pod network for {}', service
        K8sMaster service = service
        CanalPlugin canal = service.plugins.canal
        withRemoteTempFile { File tmp ->
            def replace = ""
            replace += isNoneBlank(canal.iface) ? "sed -ie 's/  canal_iface: \"\"/  canal_iface: \"${canal.iface}\"/' ${tmp}" : ""
            shell """
wget -O ${tmp} https://raw.githubusercontent.com/projectcalico/canal/master/k8s-install/1.7/canal.yaml
${replace}
kubectl apply -f https://raw.githubusercontent.com/projectcalico/canal/master/k8s-install/1.7/rbac.yaml
kubectl apply -f ${tmp}
""" call()
        }
    }

    @Inject
    void setKubectlClusterLinuxFactory(KubectlClusterDebianFactory factory) {
        this.kubectlClusterLinux = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }

    @Override
    AbstractKubectlLinux getKubectlCluster() {
        kubectlClusterLinux
    }
}
