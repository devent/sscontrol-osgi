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
package com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_8

import static com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractService.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClient
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClientFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials

import groovy.util.logging.Slf4j

/**
 * Backup service for Kubernetes.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class BackupLinux extends ScriptBase {

    @Inject
    BackupLinuxProperties propertiesProvider

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    BackupWorkerImplFactory backupWorkerFactory

    Deployment deploy

    Deployment rsync

    RsyncClient rsyncClient

    @Inject
    void setDeploymentFactory(DeploymentFactory factory) {
        Backup service = service
        this.deploy = factory.create(service.cluster, kubectl, service.service)
        this.rsync = factory.create(service.cluster, kubectl, createService([namespace: service.service.namespace, name: "rsync-${service.service.name}"]))
    }

    @Inject
    void setRsyncClientFactory(RsyncClientFactory factory) {
        Backup service = service
        this.rsyncClient = factory.create(this, service.service, service.clusterHosts, service.client, service.destination)
    }

    @Override
    def run() {
        setupDefaults()
        Backup service = service
        assertThat "clusters=0 for $service", service.clusterHosts.size(), greaterThan(0)
        setupHost service.cluster
        def sources = service.sources
        backupWorkerFactory.create(service, deploy, rsync).with {
            init()
            try {
                before()
                start { Map args ->
                    sources.each { Source source ->
                        rsyncClient.start(backup: true, path: source.source, dir: service.destination.dir, port: args.rsyncPort)
                    }
                }
            } finally {
                try {
                    after()
                } finally {
                    finally1()
                }
            }
        }
    }

    def setupDefaults() {
        Backup service = service
        if (!service.client.timeout) {
            service.client.timeout = timeoutLong
        }
        if (service.sources.size() == 0) {
            service.source defaultServiceSource
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
        getScriptNumberProperty 'default_server_port_unsecured'
    }

    int getDefaultServerPortSecured() {
        getScriptNumberProperty 'default_server_port_secured'
    }

    String getDefaultServerProtoUnsecured() {
        getScriptProperty 'default_server_proto_unsecured'
    }

    String getDefaultServerProtoSecured() {
        getScriptProperty 'default_server_proto_secured'
    }

    String getDefaultServiceSource() {
        getScriptProperty 'default_service_source'
    }

    @Override
    def getLog() {
        log
    }
}
