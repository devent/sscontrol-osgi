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
package com.anrisoftware.sscontrol.k8sbase.base.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.k8sbase.base.external.Cluster;
import com.anrisoftware.sscontrol.k8sbase.base.external.K8s;
import com.anrisoftware.sscontrol.k8sbase.base.external.K8sService;
import com.anrisoftware.sscontrol.k8sbase.base.external.Kubelet;
import com.anrisoftware.sscontrol.k8sbase.base.external.Plugin;
import com.anrisoftware.sscontrol.k8sbase.base.external.Plugin.PluginFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.k8sbase.base.internal.KubeletImpl.KubeletImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.external.host.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
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

    private final List<SshHost> targets;

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
    K8sImpl(K8sImplLogger log, ClusterImplFactory clusterFactory,
            Map<String, PluginFactory> pluginFactories,
            HostPropertiesService propertiesService,
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
        List<SshHost> l = InvokerHelper.asList(v);
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
        this.cluster = clusterFactory.create(a);
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

    /**
     * <pre>
     * privileged true
     * </pre>
     */
    @Override
    public void privileged(boolean allow) {
        this.allowPrivileged = allow;
        log.allowPrivilegedSet(this, allow);
    }

    /**
     * <pre>
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    @Override
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    /**
     * <pre>
     * kubelet port: 10250
     * </pre>
     */
    @Override
    public Kubelet kubelet(Map<String, Object> args) {
        this.kubelet = kubeletFactory.create(args);
        log.kubeletSet(this, kubelet);
        return kubelet;
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
    public void addTargets(List<SshHost> list) {
        this.targets.addAll(list);
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
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            addTargets((List<SshHost>) v);
        }
        v = args.get("runtime");
        if (v != null) {
            setContainerRuntime(v.toString());
        }
        Object clusterAdvertise = args.get("advertise");
        Object clusterApi = args.get("api");
        if (clusterAdvertise != null || clusterApi != null) {
            this.cluster = clusterFactory.create(args);
        }
    }

}
