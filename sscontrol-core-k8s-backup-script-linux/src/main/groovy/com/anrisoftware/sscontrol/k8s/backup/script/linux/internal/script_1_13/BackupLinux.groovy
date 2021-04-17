/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_13

import static com.anrisoftware.sscontrol.k8s.backup.client.external.AbstractService.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClient
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClientFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source
import com.anrisoftware.sscontrol.k8s.backup.client.internal.DeploymentImpl
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.anrisoftware.sscontrol.k8s.backup.service.internal.ServiceImpl.ServiceImplFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory

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

    @Inject
    DeploymentFactory deployFactory

    @Inject
    ServiceImplFactory serviceFactory

    DeploymentImpl deploy

    DeploymentImpl rsync

    RsyncClient rsyncClient

    KubectlClusterLinux kubectl

    @Inject
    void setRsyncClientFactory(RsyncClientFactory factory) {
        Backup service = service
        this.rsyncClient = factory.create(this, service.service, service.clusterHost, service.client, service.destination)
    }

    @Inject
    void setKubectlClusterLinuxFactory(KubectlClusterLinuxFactory factory) {
        this.kubectl = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        Backup service = service
        assertThat "clusters=0 for $service", service.clusterHosts.size(), greaterThan(0)
        this.deploy = deployFactory.create(service.clusterHost, kubectl, service.service)
        this.rsync = deployFactory.create(service.clusterHost, kubectl, serviceFactory.create([name: "rsync-${service.service.name}", namespace: service.service.namespace]))
        setupDefaults()
        kubectl.setupHosts service.clusterHosts
        def sources = service.sources
        backupWorkerFactory.create(this, service, deploy, rsync).with {
            try {
                init()
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

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    String getDefaultServiceSource() {
        getScriptProperty 'default_service_source'
    }

    @Override
    def getLog() {
        log
    }
}
