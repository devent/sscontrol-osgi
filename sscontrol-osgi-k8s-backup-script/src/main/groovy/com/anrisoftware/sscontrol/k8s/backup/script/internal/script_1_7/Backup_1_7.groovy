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
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7.Deployment.DeploymentFactory
import com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7.RsyncClient.RsyncClientFactory
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterFactory
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials

import groovy.util.logging.Slf4j

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

    Deployment deployment

    RsyncClient client

    @Inject
    void setDeploymentFactory(DeploymentFactory factory) {
        this.deployment = factory.create(service)
    }

    @Inject
    void setRsyncClientFactory(RsyncClientFactory factory) {
        this.client = factory.create(this, service)
    }

    @Override
    def run() {
        setupDefaults()
        Backup service = service
        assertThat "clusters=0 for $service", service.clusters.size(), greaterThan(0)
        setupHost service.cluster
        deployment.with {
            createClient()
            def type = service.destination.type
            def rsyncDeploy = getDeployment "rsync-${service.service.name}"
            def serviceDeploy = getDeployment service.service.name
            def oldScale = serviceDeploy.get().spec.replicas
            scaleDeployment rsyncDeploy, 1
            def rsyncService = createPublicService rsyncDeploy
            def rsyncPort = rsyncService.spec.ports[0].nodePort
            try {
                //scaleDeployment serviceDeploy, 0
                client.start(port: rsyncPort)
            } finally {
                deleteService rsyncService
                scaleDeployment serviceDeploy, oldScale
                scaleDeployment rsyncDeploy, 0
            }
        }
    }

    def setupDefaults() {
        Backup service = service
        if (!service.client.timeout) {
            service.client.timeout = timeoutLong
        }
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
        return host
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
