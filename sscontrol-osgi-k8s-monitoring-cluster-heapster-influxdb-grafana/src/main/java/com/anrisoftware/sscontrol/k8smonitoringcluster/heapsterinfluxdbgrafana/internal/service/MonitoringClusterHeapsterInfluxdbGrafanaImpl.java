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
package com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.service;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.external.MonitoringClusterHeapsterInfluxdbGrafana;
import com.anrisoftware.sscontrol.types.external.ClusterHost;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.HostServiceService;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * Cluster monitoring based on Heapster, InfluxDB and Grafana service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class MonitoringClusterHeapsterInfluxdbGrafanaImpl
        implements MonitoringClusterHeapsterInfluxdbGrafana {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface MonitoringClusterHeapsterInfluxdbGrafanaImplFactory
            extends HostServiceService {

    }

    private final MonitoringClusterHeapsterInfluxdbGrafanaImplLogger log;

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> targets;

    private final List<ClusterHost> clusters;

    @Inject
    MonitoringClusterHeapsterInfluxdbGrafanaImpl(
            MonitoringClusterHeapsterInfluxdbGrafanaImplLogger log,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.clusters = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * property << 'name=value'
     * </pre>
     */
    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    /**
     * <pre>
     * target name: 'master'
     * </pre>
     */
    public void target(Map<String, Object> args) {
        Object v = args.get("target");
        @SuppressWarnings("unchecked")
        List<SshHost> l = InvokerHelper.asList(v);
        targets.addAll(l);
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    public void addTargets(List<SshHost> list) {
        this.targets.addAll(list);
    }

    @Override
    public List<SshHost> getTargets() {
        return Collections.unmodifiableList(targets);
    }

    /**
     * <pre>
     * cluster name: 'master'
     * </pre>
     */
    public void cluster(Map<String, Object> args) {
        Object v = args.get("clusters");
        @SuppressWarnings("unchecked")
        List<ClusterHost> l = InvokerHelper.asList(v);
        clusters.addAll(l);
    }

    public ClusterHost getCluster() {
        return getClusters().get(0);
    }

    public void addClusters(List<ClusterHost> list) {
        this.clusters.addAll(list);
        log.clustersAdded(this, list);
    }

    @Override
    public List<ClusterHost> getClusters() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "monitoring-cluster-heapster-influxdb-grafana";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets()).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            addTargets((List<SshHost>) v);
        }
        v = args.get("clusters");
        if (v != null) {
            addClusters((List<ClusterHost>) v);
        }
    }

}
