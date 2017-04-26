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

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.fromreposiroty.external.FromRepository;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.external.cluster.ClusterHost;
import com.anrisoftware.sscontrol.types.external.host.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.host.HostServiceService;
import com.anrisoftware.sscontrol.types.external.repo.RepoHost;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
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

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> targets;

    private final List<ClusterHost> clusters;

    private final List<RepoHost> repos;

    @Inject
    FromRepositoryImpl(FromRepositoryImplLogger log,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<>();
        this.clusters = new ArrayList<>();
        this.repos = new ArrayList<>();
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
                .toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseClusters(args);
        parseRepos(args);
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        assertThat("targets=null", v, notNullValue());
        addTargets((List<SshHost>) v);
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

}
