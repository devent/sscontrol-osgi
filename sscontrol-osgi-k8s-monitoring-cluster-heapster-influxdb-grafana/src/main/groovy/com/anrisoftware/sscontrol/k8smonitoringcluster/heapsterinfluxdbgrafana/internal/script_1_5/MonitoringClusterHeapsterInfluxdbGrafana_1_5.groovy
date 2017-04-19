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
import com.anrisoftware.resources.templates.external.Templates
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.external.MonitoringClusterHeapsterInfluxdbGrafana
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

    Templates templates

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        this.templates = templatesFactory.create('MonitoringClusterHeapsterInfluxdbGrafana_1_5_Templates')
    }

    @Override
    def run() {
        MonitoringClusterHeapsterInfluxdbGrafana service = this.service
        def cluster = k8sCluster_1_5_Linux_Service.create(scriptsRepository, service, target, threads, scriptEnv)
        cluster.uploadCertificates credentials: service.cluster.cluster.credentials, clusterName: service.cluster.cluster.cluster.name
        File dir = createTmpDir()
        try {
            def file = "$dir/grafana-service.yaml"
            template resource: templates.getResource('grafana_service'), name: 'grafanaService', dest: file, vars: [:] call()
            cluster.runKubectl service: service, cluster: service.cluster, args: "apply -f $file"
            file = "$dir/heapster-controller.yaml"
            template resource: templates.getResource('heapster_controller'), name: 'heapsterController', dest: file, vars: [:] call()
            cluster.runKubectl service: service, cluster: service.cluster, args: "apply -f $file"
            file = "$dir/heapster-service.yaml"
            template resource: templates.getResource('heapster_service'), name: 'heapsterService', dest: file, vars: [:] call()
            cluster.runKubectl service: service, cluster: service.cluster, args: "apply -f $file"
            file = "$dir/influxdb-grafana-controller.yaml"
            template resource: templates.getResource('influxdb_grafana_controller'), name: 'influxdbGrafanaController', dest: file, vars: [:] call()
            cluster.runKubectl service: service, cluster: service.cluster, args: "apply -f $file"
            file = "$dir/influxdb-service.yaml"
            template resource: templates.getResource('influxdb_service'), name: 'influxdbService', dest: file, vars: [:] call()
            cluster.runKubectl service: service, cluster: service.cluster, args: "apply -f $file"
        } finally {
            dir.deleteDir()
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    String getHeapsterVersion() {
        properties.getProperty 'heapster_version', defaultProperties
    }

    String getHeapsterImage() {
        properties.getProperty 'heapster_image', defaultProperties
    }

    String getResizerVersion() {
        properties.getProperty 'resizer_version', defaultProperties
    }

    String getResizerImage() {
        properties.getProperty 'resizer_image', defaultProperties
    }

    String getInfluxGrafanaVersion() {
        properties.getProperty 'influx_grafana_version', defaultProperties
    }

    String getHeapsterInfluxdbVersion() {
        properties.getProperty 'heapster_influxdb_version', defaultProperties
    }

    String getHeapsterGrafanaVersion() {
        properties.getProperty 'heapster_grafana_version', defaultProperties
    }

    String getNannyMemory() {
        int nannyMemory = properties.getNumberProperty 'nanny_memory', defaultProperties
        int memory = nannyMemory * 1024 + numNodes * nannyMemoryPerNode
        "${memory}Ki"
    }

    String getBaseMetricsCpu() {
        properties.getProperty 'base_metrics_cpu', defaultProperties
    }

    String getMetricsCpuPerNode() {
        properties.getProperty 'metrics_cpu_per_node', defaultProperties
    }

    String getBaseMetricsMemory() {
        properties.getProperty 'base_metrics_memory', defaultProperties
    }

    String getMetricsMemoryPerNode() {
        properties.getProperty 'metrics_memory_per_node', defaultProperties
    }

    String getBaseEventerMemory() {
        properties.getProperty 'base_eventer_memory', defaultProperties
    }

    String getEventerMemoryPerNode() {
        properties.getProperty 'eventer_memory_per_node', defaultProperties
    }

    int getNannyMemoryPerNode() {
        properties.getNumberProperty 'nanny_memory_per_node', defaultProperties
    }

    int getNumNodes() {
        properties.getNumberProperty 'num_nodes', defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
