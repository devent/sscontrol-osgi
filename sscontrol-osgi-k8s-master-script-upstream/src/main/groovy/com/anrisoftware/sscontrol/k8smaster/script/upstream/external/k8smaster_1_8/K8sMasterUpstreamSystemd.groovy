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
package com.anrisoftware.sscontrol.k8smaster.script.upstream.external.k8smaster_1_8

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_8.linux.AbstractK8sUpstreamLinux
import com.anrisoftware.sscontrol.k8smaster.service.external.K8sMaster
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master service from the upstream sources for Systemd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sMasterUpstreamSystemd extends AbstractK8sUpstreamLinux {

    def setupClusterDefaults() {
        K8sMaster service = service
        assertThat("cluster advertise address=null", service.cluster.advertiseAddress, not(isEmptyOrNullString()))
        super.setupClusterDefaults()
    }

    def setupApiServersDefaults() {
        log.debug 'Setup api-servers hosts defaults for {}', service
        K8sMaster service = service
        if (service.cluster.apiServers.size() == 0) {
            service.cluster.apiServers << defaultApiServerHost
        }
    }

    def setupAdmissionsDefaults() {
        log.debug 'Setup admissions defaults for {}', service
        K8sMaster service = service
        if (service.admissions.size() == 0) {
            service.admissions.addAll defaultAdmissions
        }
    }

    def setupAccountDefaults() {
        log.debug 'Setup account defaults for {}', service
        K8sMaster service = service
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

    def uploadAccountCertificates() {
        log.info 'Uploads k8s-master account certificates.'
        K8sMaster service = service
        uploadTlsCerts tls: service.account.tls, name: 'account-tls'
    }

    def uploadAuthenticationsCertificates() {
        K8sMaster service = service
        service.authentications.findAll { it.tls  } each {
            Tls tls = it.tls
            uploadTlsCerts tls: tls, name: it.toString()
        }
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
        if (deployCalico && havePluginCalico) {
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

    def startAddons() {
        K8sMaster service = service
        if (deployKubeDns) {
            log.info 'Start kube-dns.'
            shell resource: addonsCmd, name: 'waitApi', timeout: timeoutVeryLong call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-de.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-svc.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-autoscaler-de.yaml'] call()
        }
        if (deployKubeDashboard) {
            log.info 'Start heapster.'
            shell resource: addonsCmd, name: 'waitApi', timeout: timeoutVeryLong call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dashboard-de.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dashboard-svc.yaml'] call()
        }
    }

    def startCalico() {
        K8sMaster service = service
        if (!havePluginCalico) {
            return
        }
        log.info 'Start Calico.'
        shell resource: addonsCmd, name: 'waitApi', timeout: timeoutVeryLong call()
        shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'calico.yaml'] call()
    }

    String getDefaultApiServerHost() {
        properties.getProperty "default_api_server_host", defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
