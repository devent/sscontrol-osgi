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
package com.anrisoftware.sscontrol.k8scluster.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.k8scluster.service.external.Cluster;
import com.anrisoftware.sscontrol.k8scluster.service.external.Context;
import com.anrisoftware.sscontrol.k8scluster.service.external.ContextFactory;
import com.anrisoftware.sscontrol.k8scluster.service.external.CredentialsFactory;
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sCluster;
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterHost;
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterHostFactory;
import com.anrisoftware.sscontrol.k8scluster.service.internal.ClusterImpl.ClusterImplFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.cluster.external.Credentials;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s-Cluster</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class K8sClusterImpl implements K8sCluster {

    private final K8sClusterImplLogger log;

    @Inject
    private transient Map<String, CredentialsFactory> credentialsFactories;

    private final List<Credentials> credentials;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final ClusterImplFactory clusterFactory;

    private Cluster cluster;

    private final ContextFactory contextFactory;

    private Context context;

    private final K8sClusterHostFactory clusterHostFactory;

    private String group;

    @Inject
    K8sClusterImpl(K8sClusterImplLogger log,
            HostServicePropertiesService propertiesService,
            ClusterImplFactory clusterFactory, ContextFactory contextFactory,
            K8sClusterHostFactory clusterHostFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.credentials = new ArrayList<>();
        this.clusterFactory = clusterFactory;
        this.contextFactory = contextFactory;
        this.clusterHostFactory = clusterHostFactory;
        this.group = "default";
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
        List<TargetHost> l = InvokerHelper.asList(v);
        targets.addAll(l);
    }

    /**
     * <pre>
     * cluster name: "default-cluster"
     * </pre>
     */
    public void cluster(Map<String, Object> args) {
        if (cluster != null) {
            this.cluster = clusterFactory.create(cluster, args);
        } else {
            this.cluster = clusterFactory.create(args);
        }
        log.clusterSet(this, cluster);
    }

    /**
     * <pre>
     * context name: "default-context"
     * </pre>
     */
    public void context(Map<String, Object> args) {
        this.context = contextFactory.create(args);
        log.contextSet(this, context);
    }

    /**
     * <pre>
     * credentials 'cert', name: 'default-admin', ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
     * </pre>
     */
    public void credentials(Map<String, Object> args, String type) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("type", type);
        credentials(a);
    }

    /**
     * <pre>
     * credentials type: 'cert', name: 'default-admin', ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
     * </pre>
     */
    public void credentials(Map<String, Object> args) {
        Object type = args.get("type");
        assertThat("type=null", type, notNullValue());
        CredentialsFactory factory = credentialsFactories.get(type.toString());
        Credentials c = factory.create(args);
        credentials.add(c);
        log.credentialsAdded(this, c);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

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
        return "k8s-cluster";
    }

    @Override
    public Cluster getCluster() {
        return cluster;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public List<Credentials> getCredentials() {
        return credentials;
    }

    @Override
    public String getGroup() {
        return group;
    }

    /**
     * Returns the hosts from where kubectl can be called, optionally with the
     * credentials for authentication.
     */
    @Override
    public List<ClusterHost> getHosts() {
        List<ClusterHost> list = new ArrayList<>();
        List<Credentials> creds = new ArrayList<>(credentials);
        for (TargetHost ssh : targets) {
            K8sClusterHost host;
            host = clusterHostFactory.create(this, ssh, creds);
            list.add(host);
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseGroup(args);
        parseCluster(args);
        parseContext(args);
    }

    private void parseGroup(Map<String, Object> args) {
        Object v = args.get("group");
        if (v != null) {
            this.group = v.toString();
        }
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

    private void parseContext(Map<String, Object> args) {
        Object v = args.get("context");
        Map<String, Object> a = new HashMap<>(args);
        a.put("name", v);
        context(a);
    }

    private void parseCluster(Map<String, Object> args) {
        Object v = args.get("cluster");
        Map<String, Object> a = new HashMap<>(args);
        a.put("name", v);
        cluster(a);
    }

}
