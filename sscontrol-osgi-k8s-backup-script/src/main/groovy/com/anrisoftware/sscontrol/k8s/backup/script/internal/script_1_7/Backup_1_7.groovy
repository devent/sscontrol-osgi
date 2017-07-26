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
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterScript

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

    def templates

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create('MonitoringClusterHeapsterInfluxdbGrafana_1_5_Templates')
    }

    @Override
    def run() {
        Backup service = service
        assertThat "clusters=0 for $service", service.clusters.size(), greaterThan(0)
        K8sClusterScript cluster = clusterFactory.create(scriptsRepository, service, target, threads, scriptEnv)
        def dir = createTmpDir()
        cluster.uploadCertificates credentials: service.cluster.cluster.credentials, clusterName: service.cluster.cluster.cluster.name
        cluster.runKubectl chdir: dir, service: service, cluster: service.cluster, args: "apply -f $it"
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    List getKubectlFilesPatterns() {
        properties.getListProperty 'kubectl_files_patterns', defaultProperties
    }

    List getDockerfileFilesPatterns () {
        properties.getListProperty 'dockerfiles_files_patterns', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
