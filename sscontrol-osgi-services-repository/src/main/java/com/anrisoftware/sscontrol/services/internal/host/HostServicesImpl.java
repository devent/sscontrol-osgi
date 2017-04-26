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
package com.anrisoftware.sscontrol.services.internal.host;

import static java.lang.String.format;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.types.external.cluster.Cluster;
import com.anrisoftware.sscontrol.types.external.cluster.ClusterHost;
import com.anrisoftware.sscontrol.types.external.cluster.Clusters;
import com.anrisoftware.sscontrol.types.external.cluster.ClustersService;
import com.anrisoftware.sscontrol.types.external.host.HostService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.host.HostServiceService;
import com.anrisoftware.sscontrol.types.external.host.HostServices;
import com.anrisoftware.sscontrol.types.external.host.HostServicesService;
import com.anrisoftware.sscontrol.types.external.host.HostTargets;
import com.anrisoftware.sscontrol.types.external.host.PreHostService;
import com.anrisoftware.sscontrol.types.external.repo.Repo;
import com.anrisoftware.sscontrol.types.external.repo.RepoHost;
import com.anrisoftware.sscontrol.types.external.repo.Repos;
import com.anrisoftware.sscontrol.types.external.repo.ReposService;
import com.anrisoftware.sscontrol.types.external.ssh.Ssh;
import com.anrisoftware.sscontrol.types.external.ssh.SshHost;
import com.anrisoftware.sscontrol.types.external.ssh.TargetServiceService;
import com.anrisoftware.sscontrol.types.external.ssh.Targets;
import com.anrisoftware.sscontrol.types.external.ssh.TargetsService;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostServicesImpl implements HostServices {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface HostServicesImplFactory extends HostServicesService {

    }

    private final Map<String, HostServiceService> availableServices;

    private final Map<String, PreHostService> availablePreServices;

    private final Map<String, HostServiceScriptService> availableScriptServices;

    private final Map<String, List<HostService>> hostServices;

    private final Targets targets;

    private final Clusters clusters;

    private final Repos repos;

    private final GetTargets<SshHost, Ssh> getTargets;

    private final GetTargets<ClusterHost, Cluster> getClusters;

    @Inject
    private HostServicesImplLogger log;

    private GetTargets<RepoHost, Repo> getRepos;

    @AssistedInject
    HostServicesImpl(TargetsService targetsService,
            ClustersService clustersService, ReposService reposService) {
        this.targets = targetsService.create();
        this.clusters = clustersService.create();
        this.repos = reposService.create();
        this.availableServices = synchronizedMap(
                new HashMap<String, HostServiceService>());
        this.availablePreServices = synchronizedMap(
                new HashMap<String, PreHostService>());
        this.hostServices = synchronizedMap(
                new LinkedHashMap<String, List<HostService>>());
        this.availableScriptServices = synchronizedMap(
                new HashMap<String, HostServiceScriptService>());
        this.getTargets = new GetTargets<>(SshHost.class, Ssh.class, "target");
        this.getClusters = new GetTargets<ClusterHost, Cluster>(
                ClusterHost.class, Cluster.class, "cluster") {

            @Override
            public List<ClusterHost> getTargets(
                    HostTargets<ClusterHost, Cluster> targets, String name) {
                try {
                    return targets.getHosts(name);
                } catch (AssertionError e) {
                    return Collections.emptyList();
                }
            }

        };
        this.getRepos = new GetTargets<RepoHost, Repo>(RepoHost.class,
                Repo.class, "repo") {

            @Override
            public List<RepoHost> getTargets(
                    HostTargets<RepoHost, Repo> targets, String name) {
                try {
                    return targets.getHosts(name);
                } catch (AssertionError e) {
                    return Collections.emptyList();
                }
            }

        };
    }

    /**
     * <pre>
     * service 'ssh'
     * </pre>
     */
    public HostService call(String name) {
        return call(new HashMap<String, Object>(), name);
    }

    /**
     * <pre>
     * service 'ssh', host: "192.168.0.3"
     * </pre>
     */
    public HostService call(Map<String, Object> args, String name) {
        HostServiceService service = availableServices.get(name);
        checkService(name, service);
        Map<String, Object> a = parseArgs(service, args);
        HostService hostService = service.create(a);
        getTargets.setupTargets(targets, hostService);
        getClusters.setupTargets(clusters, hostService);
        getRepos.setupTargets(repos, hostService);
        addService(name, hostService);
        return hostService;
    }

    /**
     * Returns the target hosts with the specified group name.
     *
     * <pre>
     * targets "master" each {
     * }
     * </pre>
     */
    public List<SshHost> targets(String name) {
        return getTargets.getTargets(targets, name);
    }

    /**
     * Returns the clusters with the specified group name.
     *
     * <pre>
     * targets "master" each {
     * }
     * </pre>
     */
    public List<ClusterHost> clusters(String name) {
        return getClusters.getTargets(clusters, name);
    }

    /**
     * Returns the code repositories with the specified group name.
     *
     * <pre>
     * repos "wordpress-app" each {
     * }
     * </pre>
     */
    public List<RepoHost> repos(String name) {
        return getRepos.getTargets(repos, name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends HostServiceService> T getAvailableService(String name) {
        return (T) availableServices.get(name);
    }

    @Override
    public Set<String> getAvailableServices() {
        return availableServices.keySet();
    }

    @Override
    public void putAvailableService(String name, HostServiceService service) {
        availableServices.put(name, service);
        log.availableServiceAdded(this, name, service);
    }

    @Override
    public void removeAvailableService(String name) {
        availableServices.remove(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PreHostService> T getAvailablePreService(String name) {
        return (T) availablePreServices.get(name);
    }

    @Override
    public void putAvailablePreService(String name, PreHostService service) {
        availablePreServices.put(name, service);
    }

    @Override
    public void removeAvailablePreService(String name) {
        availablePreServices.remove(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends HostServiceScriptService> T getAvailableScriptService(
            String name) {
        return (T) availableScriptServices.get(name);
    }

    @Override
    public void putAvailableScriptService(String name,
            HostServiceScriptService service) {
        availableScriptServices.put(name, service);
    }

    @Override
    public void removeAvailableScriptService(String name) {
        availableScriptServices.remove(name);
    }

    @Override
    public Set<String> getAvailableScriptServices() {
        return availableScriptServices.keySet();
    }

    @Override
    public List<HostService> getServices(String name) {
        return hostServices.get(name);
    }

    @Override
    public Set<String> getServices() {
        return hostServices.keySet();
    }

    @Override
    public void addService(String name, HostService service) {
        List<HostService> services = hostServices.get(name);
        if (services == null) {
            services = synchronizedList(new ArrayList<HostService>());
            hostServices.put(name, services);
        }
        services.add(service);
        log.addService(this, name, service);
    }

    @Override
    public void removeService(String name, int index) {
        List<HostService> services = hostServices.get(name);
        if (services != null) {
            services.remove(index);
        }
    }

    @Override
    public Targets getTargets() {
        return targets;
    }

    @Override
    public Clusters getClusters() {
        return clusters;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("available service", getAvailableServices())
                .append("available script service",
                        getAvailableScriptServices())
                .append("services", getServices()).toString();
    }

    private Map<String, Object> parseArgs(HostServiceService service,
            Map<String, Object> args) {
        Map<String, Object> result = new HashMap<>(args);
        if (!(service instanceof TargetServiceService)) {
            List<SshHost> t = getTargets.parseTarget(targets, args);
            result.put("targets", t);
            log.targetsInjected(this, service.getName(), t);
            List<ClusterHost> c = getClusters.parseTarget(clusters, args);
            result.put("clusters", c);
            log.clustersInjected(this, service.getName(), c);
            List<RepoHost> r = getRepos.parseTarget(repos, args);
            result.put("repos", r);
            log.reposInjected(this, service.getName(), r);
        }
        return unmodifiableMap(result);
    }

    private void checkService(String name, HostServiceService service) {
        if (service == null) {
            throw new NullPointerException(
                    format("Service '%s' not found.", name));
        }
    }

}