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
package com.anrisoftware.sscontrol.k8scluster.upstream.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.k8sbase.upstream.external.Kubectl_1_5_Upstream
import com.anrisoftware.sscontrol.k8scluster.external.K8sCluster
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer

import groovy.util.logging.Slf4j

/**
 * Configures the K8sCluster-Cluster 1.5 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sCluster_1_5_Upstream extends Kubectl_1_5_Upstream {

    TemplateResource kubectlTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def attr = [renderers: [new UriBase64Renderer()]]
        def templates = templatesFactory.create('K8sCluster_1_5_Upstream_Templates', attr)
        this.kubectlTemplate = templates.getResource('kubectl_cmd')
    }

    def setupMiscDefaults() {
        log.debug 'Setup misc defaults for {}', service
        K8sCluster service = service
        service.credentials.findAll { it.tls  } each {
            Tls tls = it.tls
            if (tls.cert) {
                tls.certName = defaultCredentialsTlsCertName
            }
            if (tls.key) {
                tls.keyName = defaultCredentialsTlsKeyName
            }
            if (tls.ca) {
                tls.caName = defaultCredentialsTlsCaName
            }
        }
    }

    def uploadCertificates() {
        log.info 'Uploads k8s-cluster certificates.'
        def certsdir = certsDir
        K8sCluster service = service
        service.credentials.findAll { it.tls  } each {
            Tls tls = it.tls
            uploadTlsCerts tls: tls, name: 'client-tls'
        }
    }

    def startKubeDnsAddon() {
        K8sCluster service = service
        if (deployKubeDns) {
            log.info 'Start kube-dns.'
            shell resource: addonsCmd, name: 'waitApi', timeout: timeoutVeryLong call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-de.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-svc.yaml'] call()
            shell resource: addonsCmd, name: 'startAddon', vars: [manifestFile: 'kube-dns-autoscaler-de.yaml'] call()
        }
    }

    def getDefaultCredentialsTlsCaName() {
        properties.getProperty 'default_credentials_tls_ca_name', defaultProperties
    }

    def getDefaultCredentialsTlsCertName() {
        properties.getProperty 'default_credentials_tls_cert_name', defaultProperties
    }

    def getDefaultCredentialsTlsKeyName() {
        properties.getProperty 'default_credentials_tls_key_name', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
