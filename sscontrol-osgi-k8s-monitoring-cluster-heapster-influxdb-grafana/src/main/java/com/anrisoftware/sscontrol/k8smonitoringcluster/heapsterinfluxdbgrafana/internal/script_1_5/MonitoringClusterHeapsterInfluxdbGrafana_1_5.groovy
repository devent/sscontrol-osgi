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
package com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.script_1_5

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Cluster monitoring based on Heapster, InfluxDB and Grafana service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class MonitoringClusterHeapsterInfluxdbGrafana_1_5 extends ScriptBase {

    @Inject
    MonitoringClusterHeapsterInfluxdbGrafana_1_5_Properties debianPropertiesProvider

    @Inject
    HostServiceScriptService k8sCluster_1_5_Linux_Service

    @Override
    def run() {
        def cluster = k8sCluster_1_5_Linux_Service.create(scriptsRepository, service, target, threads, scriptEnv)
        def file
        cluster.runKubectl service: service, file: file
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
