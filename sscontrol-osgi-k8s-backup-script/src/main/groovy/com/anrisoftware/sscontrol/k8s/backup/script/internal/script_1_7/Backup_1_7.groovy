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
package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterHost
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.KubernetesClient

/**
 * Backup service for Kubernetes 1.7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Backup_1_7 extends ScriptBase {

    @Inject
    Backup_1_7_Properties propertiesProvider

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    UriBase64Renderer uriBase64Renderer

    def templates

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create('MonitoringClusterHeapsterInfluxdbGrafana_1_5_Templates')
    }

    @Override
    def run() {
        Backup service = service
        assertThat "clusters=0 for $service", service.clusters.size(), greaterThan(0)
        def type = service.destination.type
        def replicas = getReplicas()
    }

    int getReplicas() {
        Backup service = service
        String namespace = service.service.namespace
        String name = service.service.name
        K8sClusterHost host = service.cluster
        setupHost host
        def config = buildConfig host.url, host.credentials
        try  {
            KubernetesClient client = new AutoAdaptableKubernetesClient(config)
            println client.pods().list()
        } catch (Exception e) {
            e.printStackTrace()
        }
        return 0
    }

    def buildConfig(URI hostUrl, Credentials credentials) {
        def config = new ConfigBuilder().withMasterUrl(hostUrl.toString())
        if (credentials.hasProperty('tls')) {
            Tls tls = credentials.tls
            if (tls.ca) {
                config.withCaCertData uriBase64Renderer.toString(tls.ca, "base64", null)
            }
            if (tls.cert) {
                config.withClientCertData uriBase64Renderer.toString(tls.cert, "base64", null)
            }
            if (tls.key) {
                config.withClientKeyData uriBase64Renderer.toString(tls.key, "base64", null)
            }
        }
        return config.build()
    }

    /**
     * Setups the host.
     */
    def setupHost(ClusterHost host) {
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
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
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

    @Override
    def getLog() {
        log
    }
}
