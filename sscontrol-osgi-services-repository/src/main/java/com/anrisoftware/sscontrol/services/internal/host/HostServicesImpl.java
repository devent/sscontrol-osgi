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

import com.anrisoftware.sscontrol.services.internal.host.ScriptsMap.ScriptsMapFactory;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterTargetService;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.cluster.external.Clusters;
import com.anrisoftware.sscontrol.types.cluster.external.ClustersService;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.host.external.HostTargets;
import com.anrisoftware.sscontrol.types.host.external.PreHostService;
import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;
import com.anrisoftware.sscontrol.types.registry.external.Registries;
import com.anrisoftware.sscontrol.types.registry.external.RegistriesService;
import com.anrisoftware.sscontrol.types.registry.external.Registry;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.Repo;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;
import com.anrisoftware.sscontrol.types.repo.external.Repos;
import com.anrisoftware.sscontrol.types.repo.external.ReposService;
import com.anrisoftware.sscontrol.types.ssh.external.Ssh;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.anrisoftware.sscontrol.types.ssh.external.TargetServiceService;
import com.anrisoftware.sscontrol.types.ssh.external.Targets;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.anrisoftware.sscontrol.utils.systemmappings.external.DefaultScriptInfoFactory;

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

    private final Map<ScriptInfo, HostServiceScriptService> availableScriptServices;

    private final Map<String, List<HostService>> hostServices;

    private final Map<String, Object> states;

    private final Targets targets;

    private final Clusters clusters;

    private final Repos repos;

    private final Registries registries;

    private final GetTargets<SshHost, Ssh> getTargets;

    private final GetTargets<ClusterHost, ClusterTargetService> getClusters;

    private final GetTargets<RegistryHost, Registry> getRegistries;

    @Inject
    private HostServicesImplLogger log;

    private GetTargets<RepoHost, Repo> getRepos;

    @Inject
    private DefaultScriptInfoFactory scriptInfoFactory;

    @Inject
    HostServicesImpl(TargetsService targetsService,
            ClustersService clustersService, ReposService reposService,
            RegistriesService registriesService,
            ScriptsMapFactory scriptsMapFactory) {
        this.targets = targetsService.create();
        this.clusters = clustersService.create();
        this.repos = reposService.create();
        this.registries = registriesService.create();
        this.availableServices = synchronizedMap(
                new HashMap<String, HostServiceService>());
        this.availablePreServices = synchronizedMap(
                new HashMap<String, PreHostService>());
        this.hostServices = synchronizedMap(
                new LinkedHashMap<String, List<HostService>>());
        this.availableScriptServices = synchronizedMap(
                scriptsMapFactory.create());
        this.states = synchronizedMap(new HashMap<String, Object>());
        this.getTargets = new GetTargets<>(SshHost.class, Ssh.class, "target");
        this.getClusters = new GetTargets<ClusterHost, ClusterTargetService>(
                ClusterHost.class, ClusterTargetService.class, "cluster") {

            @Override
            public List<ClusterHost> getTargets(HostServiceService service,
                    HostTargets<ClusterHost, ClusterTargetService> targets, String name) {
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
            public List<RepoHost> getTargets(HostServiceService service,
                    HostTargets<RepoHost, Repo> targets, String name) {
                try {
                    return targets.getHosts(name);
                } catch (AssertionError e) {
                    return Collections.emptyList();
                }
            }

        };
        this.getRegistries = new GetTargets<RegistryHost, Registry>(
                RegistryHost.class, Registry.class, "registry") {

            @Override
            public List<RegistryHost> getTargets(HostServiceService service,
                    HostTargets<RegistryHost, Registry> targets, String name) {
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
        getRegistries.setupTargets(registries, hostService);
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
        return getTargets.getTargets(null, targets, name);
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
        return getClusters.getTargets(null, clusters, name);
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
        return getRepos.getTargets(null, repos, name);
    }

    /**
     * Returns the image registries with the specified group name.
     *
     * <pre>
     * registries "wordpress-app" each {
     * }
     * </pre>
     */
    public List<RegistryHost> registries(String name) {
        return getRegistries.getTargets(null, registries, name);
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
    public <T extends HostServiceScriptService> T getAvailableScriptService(
            String name) {
        return (T) availableScriptServices.get(scriptInfoFactory.parse(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends HostServiceScriptService> T getAvailableScriptService(
            ScriptInfo info) {
        return (T) availableScriptServices.get(info);
    }

    public void putAvailableScriptService(String name,
            HostServiceScriptService service) {
        availableScriptServices.put(scriptInfoFactory.parse(name), service);
    }

    @Override
    public void putAvailableScriptService(ScriptInfo info,
            HostServiceScriptService service) {
        availableScriptServices.put(info, service);
    }

    public void removeAvailableScriptService(String name) {
        availableScriptServices.remove(scriptInfoFactory.parse(name));
    }

    @Override
    public void removeAvailableScriptService(ScriptInfo info) {
        availableScriptServices.remove(info);
    }

    @Override
    public Set<ScriptInfo> getAvailableScriptServices() {
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
    public void putState(String name, Object state) {
        states.put(name, state);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getState(String name) {
        return (T) states.get(name);
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
        List<SshHost> t;
        List<ClusterHost> c;
        List<RepoHost> r;
        List<RegistryHost> g;
        if (!(service instanceof TargetServiceService)) {
            t = getTargets.parseTarget(service, targets, args);
            result.put("targets", t);
            log.targetsInjected(this, service.getName(), t);
            c = getClusters.parseTarget(service, clusters, args);
            result.put("clusters", c);
            log.clustersInjected(this, service.getName(), c);
            r = getRepos.parseTarget(service, repos, args);
            result.put("repos", r);
            log.reposInjected(this, service.getName(), r);
            g = getRegistries.parseTarget(service, registries, args);
            result.put("registries", g);
            log.registriesInjected(this, service.getName(), g);
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
