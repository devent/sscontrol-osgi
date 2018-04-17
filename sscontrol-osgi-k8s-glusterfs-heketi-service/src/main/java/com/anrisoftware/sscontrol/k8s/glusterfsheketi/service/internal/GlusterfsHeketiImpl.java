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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

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

import com.anrisoftware.globalpom.core.arrays.ToList;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Admin;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.GlusterfsHeketi;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Storage;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.User;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.AdminImpl.AdminImplFactory;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.StorageImpl.StorageImplFactory;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.UserImpl.UserImplFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil.GeneticListProperty;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;
import com.google.inject.assistedinject.Assisted;

import groovy.json.JsonSlurper;

/**
 * Glusterfs-Heketi service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class GlusterfsHeketiImpl implements GlusterfsHeketi {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface GlusterfsHeketiImplFactory extends HostServiceService {

    }

    private final GlusterfsHeketiImplLogger log;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final List<ClusterHost> clusters;

    private final List<RepoHost> repos;

    private final List<Object> nodes;

    private Admin admin;

    private User user;

    private final Map<String, Object> topology;

    private transient AdminImplFactory adminFactory;

    private transient UserImplFactory userFactory;

    private String labelName;

    private final Map<String, Object> vars;

    private String namespace;

    private Storage storage;

    private transient StorageImplFactory storageFactory;

    private Integer maxBrickSizeGb;

    private Integer minBrickSizeGb;

    private String serviceAddress;

    @Inject
    GlusterfsHeketiImpl(GlusterfsHeketiImplLogger log,
            HostServicePropertiesService propertiesService,
            AdminImplFactory adminFactory, UserImplFactory userFactory,
            StorageImplFactory storageFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.clusters = new ArrayList<>();
        this.repos = new ArrayList<>();
        this.adminFactory = adminFactory;
        this.admin = adminFactory.create();
        this.userFactory = userFactory;
        this.user = userFactory.create();
        this.topology = new HashMap<>();
        this.vars = new HashMap<>();
        this.storageFactory = storageFactory;
        this.storage = storageFactory.create();
        this.nodes = new ArrayList<>();
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
     * admin key: "MySecret"
     * </pre>
     */
    public void admin(Map<String, Object> args) {
        this.admin = adminFactory.create(args);
    }

    /**
     * <pre>
     * user key: "MyVolumeSecret"
     * </pre>
     */
    public void user(Map<String, Object> args) {
        this.user = userFactory.create(args);
    }

    /**
     * <pre>
     * topology parse: """
    {
    "clusters":[
    {
      "nodes":[
    """
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public void topology(Map<String, Object> args) {
        Object v = args.get("parse");
        if (v != null) {
            String parse = v.toString();
            Object object = new JsonSlurper().parseText(parse);
            putAllTopology((Map<String, Object>) object);
        }
    }

    /**
     * <pre>
     * vars << [heketi: [snapshot: [limit: 32]]]
     * vars << [tolerations: [toleration: [key: 'node-role.kubernetes.io/master', effect: 'NoSchedule']]]
     * </pre>
     */
    @Override
    public Map<String, Object> getVars() {
        return vars;
    }

    /**
     * <pre>
     * storage address: "10.2.35.3:8080"
     * </pre>
     */
    public void storage(Map<String, Object> args) {
        this.storage = storageFactory.create(args);
        log.storageSet(this, storage);
    }

    /**
     * <pre>
     * node &lt;&lt; 'node0.test'
     * node &lt;&lt; nodes
     * </pre>
     */
    public List<Object> getNode() {
        return GeneticListPropertyUtil.<Object>geneticListStatement(
                new GeneticListProperty<Object>() {

                    @Override
                    public void add(Object property) {
                        nodes.add(property);
                    }
                });
    }

    /**
     * <pre>
     * brick min: 1, max: 50
     * </pre>
     */
    public void brick(Map<String, Object> args) {
        parseMaxBrickSizeGb(args);
        parseMinBrickSizeGb(args);
    }

    /**
     * <pre>
     * service address: "10.96.10.10"
     * </pre>
     */
    public void service(Map<String, Object> args) {
        parseServiceAddress(args);
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
    public ClusterHost getClusterHost() {
        return getClusterHosts().get(0);
    }

    public void addClusterHosts(List<ClusterHost> list) {
        this.clusters.addAll(list);
        log.clustersAdded(this, list);
    }

    @Override
    public List<ClusterHost> getClusterHosts() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public RepoHost getRepo() {
        return getRepos().get(0);
    }

    @Override
    public void addRepos(List<RepoHost> list) {
        this.repos.addAll(list);
        log.reposAdded(this, list);
    }

    @Override
    public List<RepoHost> getRepos() {
        return Collections.unmodifiableList(repos);
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "glusterfs-heketi";
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String getLabelName() {
        return labelName;
    }

    @Override
    public Admin getAdmin() {
        return admin;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getTopology() {
        return topology;
    }

    private void putAllTopology(Map<String, Object> map) {
        topology.putAll(map);
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void addNodes(List<Object> nodes) {
        this.nodes.addAll(nodes);
        log.nodesAdded(this, nodes);
    }

    public void addNode(Object node) {
        this.nodes.add(node);
        log.nodeAdded(this, node);
    }

    @Override
    public List<Object> getNodes() {
        return nodes;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    public void setMinBrickSizeGb(int minBrickSizeGb) {
        this.minBrickSizeGb = minBrickSizeGb;
    }

    @Override
    public Integer getMinBrickSizeGb() {
        return minBrickSizeGb;
    }

    public void setMaxBrickSizeGb(int maxBrickSizeGb) {
        this.maxBrickSizeGb = maxBrickSizeGb;
    }

    @Override
    public Integer getMaxBrickSizeGb() {
        return maxBrickSizeGb;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    @Override
    public String getServiceAddress() {
        return serviceAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets())
                .append("clusters", getClusterHosts())
                .append("repos", getRepos()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseNodes(args);
        parseNamespace(args);
        parseLabelNode(args);
        parseTargets(args);
        parseClusters(args);
        parseRepos(args);
    }

    private void parseNodes(Map<String, Object> args) {
        Object v = args.get("nodes");
        if (v != null) {
            addNodes(ToList.toList(v));
        }
    }

    private void parseNamespace(Map<String, Object> args) {
        Object v = args.get("namespace");
        if (v != null) {
            setNamespace(v.toString());
        }
    }

    private void parseLabelNode(Map<String, Object> args) {
        Object v = args.get("name");
        if (v != null) {
            setLabelName(v.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        assertThat("targets=null", v, notNullValue());
        addTargets((List<TargetHost>) v);
    }

    @SuppressWarnings("unchecked")
    private void parseClusters(Map<String, Object> args) {
        Object v = args.get("clusters");
        assertThat("clusters=null", v, notNullValue());
        addClusterHosts((List<ClusterHost>) v);
    }

    @SuppressWarnings("unchecked")
    private void parseRepos(Map<String, Object> args) {
        Object v = args.get("repos");
        assertThat("repos=null", v, notNullValue());
        addRepos((List<RepoHost>) v);
    }

    private void parseMinBrickSizeGb(Map<String, Object> args) {
        Object v = args.get("min");
        if (v != null) {
            setMinBrickSizeGb(((Number) v).intValue());
        }
    }

    private void parseMaxBrickSizeGb(Map<String, Object> args) {
        Object v = args.get("max");
        if (v != null) {
            setMaxBrickSizeGb(((Number) v).intValue());
        }
    }

    private void parseServiceAddress(Map<String, Object> args) {
        Object v = args.get("address");
        if (v != null) {
            setServiceAddress(v.toString());
        }
    }

}
