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
package com.anrisoftware.sscontrol.k8smaster.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.globalpom.arrays.ToList;
import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.k8smaster.external.Authentication;
import com.anrisoftware.sscontrol.k8smaster.external.AuthenticationFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Authorization;
import com.anrisoftware.sscontrol.k8smaster.external.AuthorizationFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Binding;
import com.anrisoftware.sscontrol.k8smaster.external.Cluster;
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster;
import com.anrisoftware.sscontrol.k8smaster.external.K8sMasterService;
import com.anrisoftware.sscontrol.k8smaster.external.Kubelet;
import com.anrisoftware.sscontrol.k8smaster.external.Plugin;
import com.anrisoftware.sscontrol.k8smaster.external.Plugin.PluginFactory;
import com.anrisoftware.sscontrol.k8smaster.external.Tls;
import com.anrisoftware.sscontrol.k8smaster.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.k8smaster.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.k8smaster.internal.KubeletImpl.KubeletImplFactory;
import com.anrisoftware.sscontrol.k8smaster.internal.TlsImpl.TlsImplFactory;
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

    private final Map<String, Plugin> plugins;

    @Inject
    private TlsImplFactory tlsFactory;

    @Inject
    private Map<String, AuthenticationFactory> authenticationFactories;

    @Inject
    private Map<String, AuthorizationFactory> authorizationFactories;

    private DebugLogging debug;

    private Cluster cluster;

    private Boolean allowPrivileged;

    private final List<Authentication> authentications;

    private final List<Authorization> authorizations;

    private final Kubelet kubelet;

    private final List<String> admissions;

    private Tls tls;

    private Binding binding;

    private final BindingImplFactory bindingFactory;

    @Inject
    K8sMasterImpl(K8sMasterImplLogger log, ClusterImplFactory clusterFactory,
            Map<String, PluginFactory> pluginFactories,
            HostPropertiesService propertiesService,
            KubeletImplFactory kubeletFactory,
            BindingImplFactory bindingFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.clusterFactory = clusterFactory;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.cluster = clusterFactory.create();
        this.pluginFactories = pluginFactories;
        this.plugins = new LinkedHashMap<>();
        this.authentications = new ArrayList<>();
        this.authorizations = new ArrayList<>();
        this.admissions = new ArrayList<>();
        this.kubelet = kubeletFactory.create();
        this.binding = bindingFactory.create();
        this.bindingFactory = bindingFactory;
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
     * bind insecure: "127.0.0.1", secure: "0.0.0.0", port: 8080
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        this.binding = bindingFactory.create(a);
        log.bindingSet(this, binding);
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
        plugins.put(name, plugin);
        log.pluginAdded(this, plugin);
        return plugin;
    }

    /**
     * <pre>
     * privileged true
     * </pre>
     */
    public void privileged(boolean allow) {
        this.allowPrivileged = allow;
        log.allowPrivilegedSet(this, allow);
    }

    /**
     * <pre>
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    /**
     * <pre>
     * authentication "basic", file: "some_file"
     * </pre>
     */
    public void authentication(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("type", name);
        authentication(a);
    }

    /**
     * <pre>
     * authentication type: "basic", file: "some_file"
     * </pre>
     */
    public void authentication(Map<String, Object> args) {
        String name = args.get("type").toString();
        AuthenticationFactory factory = authenticationFactories.get(name);
        assertThat(format("authentication(%s)=null", name), factory,
                is(notNullValue()));
        Authentication auth = factory.create(args);
        authentications.add(auth);
        log.authenticationAdded(this, auth);
    }

    /**
     * <pre>
     * authorization "allow"
     * </pre>
     */
    public void authorization(String name) {
        Map<String, Object> a = new HashMap<>();
        a.put("mode", name);
        authorization(a);
    }

    /**
     * <pre>
     * authorization "abac", file: "policy_file.json"
     * </pre>
     */
    public void authorization(Map<String, Object> args, String name) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("mode", name);
        authorization(a);
    }

    /**
     * <pre>
     * authorization mode: "abac", abac: ""
     * </pre>
     */
    public void authorization(Map<String, Object> args) {
        String name = args.get("mode").toString();
        AuthorizationFactory factory = authorizationFactories.get(name);
        assertThat(format("authorization(%s)=null", name), factory,
                is(notNullValue()));
        Authorization auth = factory.create(args);
        authorizations.add(auth);
        log.authorizationAdded(this, auth);
    }

    /**
     * <pre>
     * admission << "AlwaysAdmit,ServiceAccount"
     * </pre>
     */
    public List<String> getAdmission() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                String[] split = StringUtils.split(property, ",");
                ToList.toList(admissions, split);
                log.admissionsAdded(K8sMasterImpl.this, property);
            }
        });
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
    public Binding getBinding() {
        return binding;
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
    public List<Authentication> getAuthentications() {
        return authentications;
    }

    @Override
    public List<Authorization> getAuthorizations() {
        return authorizations;
    }

    @Override
    public Kubelet getKubelet() {
        return kubelet;
    }

    @Override
    public List<String> getAdmissions() {
        return admissions;
    }

    @Override
    public Tls getTls() {
        return tls;
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
            targets.addAll((List<SshHost>) v);
        }
    }

}
