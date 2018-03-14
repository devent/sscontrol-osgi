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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.split;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8sService;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Label;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.LabelFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Plugin.PluginFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.Taint;
import com.anrisoftware.sscontrol.k8sbase.base.service.external.TaintFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImpl.KubeletImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s-Master</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class K8sImpl implements K8s {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sImplFactory extends K8sService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final K8sImplLogger log;

    private transient ClusterImplFactory clusterFactory;

    private transient Map<String, PluginFactory> pluginFactories;

    private final Map<String, Plugin> plugins;

    private transient TlsFactory tlsFactory;

    private DebugLogging debug;

    private Cluster cluster;

    private Boolean allowPrivileged;

    private transient KubeletImplFactory kubeletFactory;

    private Kubelet kubelet;

    private Tls tls;

    private String containerRuntime;

    @Inject
    private transient LabelFactory labelFactory;

    private final Map<String, Label> labels;

    @Inject
    private transient TaintFactory taintFactory;

    private final Map<String, Taint> taints;

    private final List<ClusterHost> clusterHosts;

    @Inject
    K8sImpl(K8sImplLogger log, ClusterImplFactory clusterFactory,
            Map<String, PluginFactory> pluginFactories,
            HostServicePropertiesService propertiesService,
            KubeletImplFactory kubeletFactory, TlsFactory tlsFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.clusterFactory = clusterFactory;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.cluster = clusterFactory.create();
        this.pluginFactories = pluginFactories;
        this.plugins = new LinkedHashMap<>();
        this.kubeletFactory = kubeletFactory;
        this.kubelet = kubeletFactory.create();
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        this.labels = new HashMap<>();
        this.taints = new HashMap<>();
        this.clusterHosts = new ArrayList<>();
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
    @Override
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
    @Override
    public void target(Map<String, Object> args) {
        Object v = args.get("target");
        @SuppressWarnings("unchecked")
        List<TargetHost> l = InvokerHelper.asList(v);
        targets.addAll(l);
    }

    /**
     * <pre>
     * debug "error", level: 1
     * </pre>
     */
    @Override
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
    @Override
    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<>(args);
        invokeMethod(debug, "debug", arguments);
    }

    /**
     * <pre>
     * debug << [name: "error", level: 1]
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getDebug() {
        return (List<Object>) invokeMethod(debug, "getDebug", null);
    }

    /**
     * <pre>
     * cluster range: "10.254.0.0/16"
     * </pre>
     */
    @Override
    public void cluster(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        if (this.cluster != null) {
            this.cluster = clusterFactory.create(this.cluster, a);
        } else {
            this.cluster = clusterFactory.create(a);
        }
        log.clusterSet(this, cluster);
    }

    /**
     * <pre>
     * plugin "etcd"
     * </pre>
     */
    @Override
    public Plugin plugin(String name) {
        Map<String, Object> a = new HashMap<>();
        a.put("name", name);
        return plugin(a);
    }

    /**
     * <pre>
     * plugin "etcd", target: "infra0"
     * </pre>
     */
    @Override
    public Plugin plugin(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("name", name);
        return plugin(a);
    }

    /**
     * <pre>
     * plugin name: "etcd", target: "infra0"
     * </pre>
     */
    @Override
    public Plugin plugin(Map<String, Object> args) {
        Object v = args.get("name");
        String name = v.toString();
        PluginFactory factory = pluginFactories.get(name);
        Map<String, Object> a = new HashMap<>(args);
        Plugin plugin = factory.create(a);
        plugins.put(name, plugin);
        log.pluginAdded(this, plugin);
        return plugin;
    }

    @Override
    public void privileged(boolean allow) {
        this.allowPrivileged = allow;
        log.allowPrivilegedSet(this, allow);
    }

    @Override
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    @Override
    public Kubelet kubelet(Map<String, Object> args) {
        this.kubelet = kubeletFactory.create(args);
        log.kubeletSet(this, kubelet);
        return kubelet;
    }

    @Override
    public void label(Map<String, Object> args) {
        Label label = labelFactory.create(args);
        log.labelAdded(this, label);
        labels.put(label.getKey(), label);
    }

    @Override
    public List<String> getLabel() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                String[] s = split(property, "=");
                Map<String, Object> args = new HashMap<>();
                args.put("key", s[0]);
                if (s.length > 1) {
                    args.put("value", s[1]);
                }
                label(args);
            }
        });
    }

    @Override
    public void taint(Map<String, Object> args) {
        Taint taint = taintFactory.create(args);
        log.taintAdded(this, taint);
        taints.put(taint.getKey(), taint);
    }

    private static final Pattern TAINT_PATTERN = Pattern
            .compile("^(?<key>.*)(:?=(?<value>.*)(:?:(?<effect>.*)))");

    @Override
    public List<String> getTaint() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                Matcher m = TAINT_PATTERN.matcher(property);
                assertThat(format("taint matches %s but was %s", TAINT_PATTERN,
                        property), m.matches(), equalTo(true));
                String key = m.group("key");
                String value = m.group("value");
                String effect = m.group("effect");
                Map<String, Object> args = new HashMap<>();
                args.put("key", key);
                args.put("value", value);
                args.put("effect", effect);
                taint(args);
            }
        });
    }

    @Override
    public DebugLogging getDebugLogging() {
        return debug;
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public void addTargets(List<TargetHost> list) {
        this.targets.addAll(list);
    }

    @Override
    public List<TargetHost> getTargets() {
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
    public Map<String, Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public Boolean isAllowPrivileged() {
        return allowPrivileged;
    }

    @Override
    public Kubelet getKubelet() {
        return kubelet;
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public void setContainerRuntime(String runtime) {
        this.containerRuntime = runtime;
    }

    @Override
    public String getContainerRuntime() {
        return containerRuntime;
    }

    @Override
    public Map<String, Label> getLabels() {
        return labels;
    }

    @Override
    public Map<String, Taint> getTaints() {
        return taints;
    }

    @Override
    public ClusterHost getClusterHost() {
        if (getClusterHosts().size() > 0) {
            return getClusterHosts().get(0);
        } else {
            return null;
        }
    }

    public void addClusterHosts(List<ClusterHost> list) {
        this.clusterHosts.addAll(list);
        log.clusterHostsAdded(this, list);
    }

    @Override
    public List<ClusterHost> getClusterHosts() {
        return Collections.unmodifiableList(clusterHosts);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseRuntime(args);
        parseCluster(args);
        parseClusters(args);
    }

    private void parseCluster(Map<String, Object> args) {
        Object name = args.get("name");
        Object advertise = args.get("advertise");
        Object api = args.get("api");
        Object join = args.get("join");
        Object host = args.get("host");
        if (name != null || advertise != null || api != null || join != null
                || host != null) {
            this.cluster = clusterFactory.create(args);
        }
    }

    private void parseRuntime(Map<String, Object> args) {
        Object v = args.get("runtime");
        if (v != null) {
            setContainerRuntime(v.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            addTargets((List<TargetHost>) v);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseClusters(Map<String, Object> args) {
        Object v = args.get("clusters");
        if (v != null) {
            addClusterHosts((List<ClusterHost>) v);
        }
    }

}
