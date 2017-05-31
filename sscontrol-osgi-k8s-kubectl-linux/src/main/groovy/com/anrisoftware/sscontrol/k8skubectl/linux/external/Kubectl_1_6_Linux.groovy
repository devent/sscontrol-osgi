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
package com.anrisoftware.sscontrol.k8skubectl.linux.external

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import java.nio.charset.StandardCharsets

import javax.inject.Inject

import org.apache.commons.io.IOUtils

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory
import com.anrisoftware.sscontrol.types.cluster.external.Cluster
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.ClusterService
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer

import groovy.util.logging.Slf4j

/**
 * Provides kubectl for GNU/Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Kubectl_1_6_Linux extends ScriptBase {

    @Inject
    TlsFactory tlsFactory

    @Inject
    CredentialsNopFactory credentialsNopFactory

    TemplateResource kubectlTemplate

    TemplateResource kubeconfTemplate

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def attr = [renderers: [new UriBase64Renderer()]]
        def templates = templatesFactory.create('Kubectl_1_6_Linux_Templates', attr)
        this.kubectlTemplate = templates.getResource('kubectl_cmd')
        this.kubeconfTemplate = templates.getResource('kubectl_conf')
    }

    def setupMiscDefaults() {
        log.debug 'Setup misc defaults for {}', service
        Cluster service = service
        if (service.credentials.size() == 0) {
            def args = [:]
            args.name = "default-admin"
            args.type = "anon"
            service.credentials << credentialsNopFactory.create(args)
        }
    }

    def uploadCertificates(Map vars) {
        log.info 'Uploads k8s-cluster certificates.'
        def certsdir = certsDir
        List credentials = vars.credentials
        String clusterName = vars.clusterName
        assertThat "credentials=null", credentials, notNullValue()
        assertThat "clusterName=null", clusterName, notNullValue()
        def args = [:]
        def certsDir = getClusterCertsDir(clusterName)
        shell privileged: true, """
mkdir -p $certsDir
chown robobee.robobee -R $certsDir
chmod o-rx $certsDir
""" call()
        credentials.findAll { it.hasProperty('tls') } each {
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
            def a = [:]
            a.dest = certsDir
            a.tls = tls
            a.name = 'client-tls'
            uploadTlsCerts a
        }
    }

    def runKubectl(Map vars) {
        log.info 'Run kubectl with {}', vars
        ClusterService service = vars.service
        ClusterHost host = vars.cluster
        assertThat "chdir!=null", vars.chdir, notNullValue()
        assertThat "service!=null", service, notNullValue()
        assertThat "host!=null", host, notNullValue()
        Credentials c = host.credentials
        setupCredentials c
        Map v = new HashMap(vars)
        v.service = service
        v.cluster = host
        v.credentials = c
        v.tls = c.hasProperty('tls') ? c.tls : null
        v.certsDir = getClusterCertsDir(host.clusterName)
        def args = new HashMap(vars)
        args.resource = kubectlTemplate
        args.name = 'kubectlCmd'
        args.vars = v
        shell args call()
    }

    /**
     * Creates and uploads kubeconfig for the cluster.
     *
     * @param vars
     * <li>kubeconfigFile: the path of the kubeconfig file on the server.
     * <li>service: the ClusterService.
     * <li>host: the ClusterHost.
     * @return the created kubeconfig file.
     */
    File uploadKubeconfig(Map vars) {
        log.info 'Uploads kubeconfig for {}.', vars
        ClusterService service = vars.service
        ClusterHost host = vars.cluster
        assertThat "kubeconfigFile!=null", vars.kubeconfigFile, notNullValue()
        assertThat "service!=null", service, notNullValue()
        assertThat "host!=null", host, notNullValue()
        Credentials c = setupCredentials host.credentials
        Map v = new HashMap(vars)
        v.certs = c.hasProperty('tls') ? certsData(c.tls) : [:]
        v.resource = kubeconfTemplate
        v.name = 'kubectlConf'
        v.dest = vars.kubeconfigFile
        template v call()
        return v.dest
    }

    /**
     * Runs kubectl with a kubeconfig file.
     *
     * @param vars
     * <ul>
     * <li>kubeconfigFile: the path of the kubeconfig file on the server.
     * <li>service: the ClusterService.
     * <li>host: the ClusterHost.
     * <li>args: kubectl arguments.
     * </ul>
     */
    def runKubectlKubeconfig(Map vars) {
        log.info 'Run kubectl with {}', vars
        ClusterService service = vars.service
        ClusterHost host = vars.cluster
        assertThat "kubeconfigFile!=null", vars.kubeconfigFile, notNullValue()
        assertThat "service!=null", service, notNullValue()
        assertThat "host!=null", host, notNullValue()
        Credentials c = host.credentials
        setupCredentials c
        Map v = new HashMap(vars)
        v.service = service
        v.cluster = host
        v.credentials = c
        def args = new HashMap(vars)
        args.resource = kubectlTemplate
        args.name = 'kubectlKubeconfigCmd'
        args.vars = v
        shell args call()
    }

    /**
     * Setups the credentials.
     */
    Credentials setupCredentials(ClusterHost host) {
        Credentials c = host.credentials
        if (!host.proto) {
            if (c.hasProperty('tls') && c.tls.ca) {
                host.proto = defaultServerProtoSecured
            } else {
                host.proto = defaultServerProtoUnsecured
            }
        }
        if (!host.port) {
            if (c.hasProperty('tls') && c.tls.ca) {
                host.port = defaultServerPortSecured
            } else {
                host.port = defaultServerPortUnsecured
            }
        }
        if (c.hasProperty('tls')) {
            Tls tls = c.tls
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
        return c
    }

    /**
     * Returns base64 encoded certificates data.
     */
    Map certsData(Tls tls) {
        def renderer = new UriBase64Renderer()
        def map = [:]
        if (tls.cert) {
            map << [cert: renderer.toString(IOUtils.toString(tls.cert, StandardCharsets.UTF_8))]
        }
        if (tls.key) {
            map << [key: renderer.toString(IOUtils.toString(tls.key, StandardCharsets.UTF_8))]
        }
        if (tls.ca) {
            map << [ca: renderer.toString(IOUtils.toString(tls.ca, StandardCharsets.UTF_8))]
        }
    }

    String getClusterCertsDir(String name) {
        String.format("%s/%s", certsDir, name)
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

    int getDefaultServerPortUnsecured() {
        properties.getNumberProperty 'default_server_port_unsecured', defaultProperties
    }

    int getDefaultServerPortSecured() {
        properties.getNumberProperty 'default_server_port_secured', defaultProperties
    }

    String getDefaultServerProtoUnsecured() {
        properties.getProperty 'default_server_proto_unsecured', defaultProperties
    }

    String getDefaultServerProtoSecured() {
        properties.getProperty 'default_server_proto_secured', defaultProperties
    }

    File getKubectlCmd() {
        getFileProperty 'kubectl_cmd', binDir
    }

    @Override
    def getLog() {
        log
    }
}
