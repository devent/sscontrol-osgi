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

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.k8sbase.upstream.external.Kubectl_1_5_Upstream
import com.anrisoftware.sscontrol.k8scluster.external.Credentials
import com.anrisoftware.sscontrol.k8scluster.external.K8sCluster
import com.anrisoftware.sscontrol.k8scluster.upstream.external.CredentialsNop.CredentialsNopFactory
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory
import com.anrisoftware.sscontrol.types.external.ClusterService
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Cluster 1.5 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sCluster_1_5_Upstream extends Kubectl_1_5_Upstream {

    @Inject
    TlsFactory tlsFactory

    @Inject
    CredentialsNopFactory credentialsNopFactory

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
        if (service.credentials.size() == 0) {
            def args = [:]
            args.name = "default-admin"
            args.type = "anon"
            service.credentials << credentialsNopFactory.create(args)
        }
        service.credentials.each { Credentials credentials ->
            if (!credentials.port) {
                if (credentials.hasProperty('tls') && credentials.tls.ca) {
                    credentials.port = defaultServerPortSecured
                } else {
                    credentials.port = defaultServerPortUnsecured
                }
            }
        }
        service.credentials.findAll { it.hasProperty('tls')  } each {
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
        def args = [:]
        if (!certsDir) {
            args.dest = createTmpDir(suffix: 'certs')
        }
        service.credentials.findAll { it.hasProperty('tls') } each {
            Tls tls = it.tls
            def a = new HashMap(args)
            a.tls = tls
            a.name = 'client-tls'
            uploadTlsCerts a
        }
    }

    def runKubectl(Map vars) {
        log.info 'Run kubectl with {}', vars
        ClusterService service = vars.service
        assertThat "service!=null", service, notNullValue()
        Map v = new HashMap(vars)
        v.cluster = this
        shell parent: service, resource: kubectlTemplate, name: 'kubectlCmd', vars: v call()
    }

    def getClusterServers() {
        def service = service as ClusterService
        assert service.clusters.size() > 0 : "service.clusters.size()>0"
        service.clusters
    }

    def getClusterTls() {
        def service = service as ClusterService
        assert service.clusters.size() > 0 : "service.clusters.size()>0"
        def credentials = service.clusters.find { it.credentials.hasProperty('tls')  }
        def args = [:]
        if (credentials) {
            args.ca = credentials.tls.ca
            args.caName = credentials.tls.caName
            args.cert = credentials.tls.cert
            args.certName = credentials.tls.certName
            args.key = credentials.tls.key
            args.keyName = credentials.tls.keyName
        }
        return tlsFactory.create(args)
    }

    def getClientTls() {
        getClusterTls()
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

    def getDefaultServerPortUnsecured() {
        properties.getNumberProperty 'default_server_port_unsecured', defaultProperties
    }

    def getDefaultServerPortSecured() {
        properties.getNumberProperty 'default_server_port_secured', defaultProperties
    }

    File getKubectlCmd() {
        properties.getFileProperty 'kubectl_cmd', binDir, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
