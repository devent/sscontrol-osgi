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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.service;

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

import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.FromRepository;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;
import com.google.inject.assistedinject.Assisted;

/**
 * From repository service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FromRepositoryImpl implements FromRepository {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface FromRepositoryImplFactory extends HostServiceService {

    }

    private final FromRepositoryImplLogger log;

    private HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final List<ClusterHost> clusters;

    private final List<RepoHost> repos;

    private final List<RegistryHost> registries;

    private final Map<String, Object> vars;

    @Inject
    FromRepositoryImpl(FromRepositoryImplLogger log,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.clusters = new ArrayList<>();
        this.repos = new ArrayList<>();
        this.registries = new ArrayList<>();
        this.vars = new HashMap<>();
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
     * vars << [mysql: [version: "5.6", image: "mysql"]]
     * </pre>
     */
    @Override
    public Map<String, Object> getVars() {
        return vars;
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
    public RegistryHost getRegistry() {
        List<RegistryHost> r = getRegistries();
        if (r.size() > 0) {
            return r.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void addRegistries(List<RegistryHost> list) {
        this.registries.addAll(list);
        log.registriesAdded(this, list);
    }

    @Override
    public List<RegistryHost> getRegistries() {
        return Collections.unmodifiableList(registries);
    }

    public void setServiceProperties(HostServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getName() {
        return "from-repository";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", getTargets())
                .append("clusters", getClusters()).append("repos", getRepos())
                .append("registries", getRegistries()).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseClusters(args);
        parseRepos(args);
        parseRegistries(args);
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
        addClusters((List<ClusterHost>) v);
    }

    @SuppressWarnings("unchecked")
    private void parseRepos(Map<String, Object> args) {
        Object v = args.get("repos");
        assertThat("repos=null", v, notNullValue());
        addRepos((List<RepoHost>) v);
    }

    @SuppressWarnings("unchecked")
    private void parseRegistries(Map<String, Object> args) {
        Object v = args.get("registries");
        if (v != null) {
            addRegistries((List<RegistryHost>) v);
        }
    }

}
