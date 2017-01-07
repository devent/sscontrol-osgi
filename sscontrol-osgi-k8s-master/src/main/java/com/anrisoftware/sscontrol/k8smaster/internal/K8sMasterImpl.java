/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.k8smaster.external.Cluster;
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster;
import com.anrisoftware.sscontrol.k8smaster.external.K8sMasterService;
import com.anrisoftware.sscontrol.k8smaster.external.Plugin;
import com.anrisoftware.sscontrol.k8smaster.external.Plugin.PluginFactory;
import com.anrisoftware.sscontrol.k8smaster.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s-Master</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class K8sMasterImpl implements K8sMaster {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sMasterImplFactory extends K8sMasterService {

    }

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    private final K8sMasterImplLogger log;

    private final ClusterImplFactory clusterFactory;

    private final Map<String, PluginFactory> pluginFactories;

    private final List<Plugin> plugins;

    private DebugLogging debug;

    private Cluster cluster;

    @Inject
    K8sMasterImpl(K8sMasterImplLogger log, ClusterImplFactory clusterFactory,
            Map<String, PluginFactory> pluginFactories,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.clusterFactory = clusterFactory;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.cluster = clusterFactory.create();
        this.pluginFactories = pluginFactories;
        this.plugins = new ArrayList<>();
        parseArgs(args);
    }

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
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

    /**
     * <pre>
     * debug "error", level: 1
     * </pre>
     */
    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<>(args);
        arguments.put("name", name);
        invokeMethod(debug, "debug", arguments);
    }

    /**
     * <pre>
     * debug name: "error", level: 1
     * </pre>
     */
    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<>(args);
        invokeMethod(debug, "debug", arguments);
    }

    /**
     * <pre>
     * debug << [name: "error", level: 1]
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public List<Object> getDebug() {
        return (List<Object>) invokeMethod(debug, "getDebug", null);
    }

    /**
     * <pre>
     * cluster range: "10.254.0.0/16"
     * </pre>
     */
    public void cluster(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        this.cluster = clusterFactory.create(a);
        log.clusterSet(this, cluster);
    }

    /**
     * <pre>
     * plugin "etcd", target: "infra0"
     * </pre>
     */
    public Plugin plugin(Map<String, Object> args, String name) {
        PluginFactory factory = pluginFactories.get(name);
        Map<String, Object> a = new HashMap<>(args);
        Plugin plugin = factory.create(a);
        plugins.add(plugin);
        log.pluginAdded(this, plugin);
        return plugin;
    }

    @Override
    public DebugLogging getDebugLogging() {
        return debug;
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<SshHost> getTargets() {
        return Collections.unmodifiableList(targets);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "k8s-master";
    }

    @Override
    public Cluster getCluster() {
        return cluster;
    }

    @Override
    public List<Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("cluster", getCluster()).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<SshHost>) v);
        }
    }

}
