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
package com.anrisoftware.sscontrol.services.internal;

import static java.lang.String.format;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.services.external.NoTargetsForServiceException;
import com.anrisoftware.sscontrol.types.external.Clusters;
import com.anrisoftware.sscontrol.types.external.ClustersService;
import com.anrisoftware.sscontrol.types.external.HostService;
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService;
import com.anrisoftware.sscontrol.types.external.HostServiceService;
import com.anrisoftware.sscontrol.types.external.HostServices;
import com.anrisoftware.sscontrol.types.external.HostServicesService;
import com.anrisoftware.sscontrol.types.external.PreHostService;
import com.anrisoftware.sscontrol.types.external.Ssh;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.Targets;
import com.anrisoftware.sscontrol.types.external.TargetsService;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostServicesImpl implements HostServices {

    private static final String DEFAULT_TARGETS_NAME = "default";

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

    @Inject
    private HostServicesImplLogger log;

    @AssistedInject
    HostServicesImpl(TargetsService targetsService,
            ClustersService clustersService) {
        this.targets = targetsService.create();
        this.clusters = clustersService.create();
        this.availableServices = synchronizedMap(
                new HashMap<String, HostServiceService>());
        this.availablePreServices = synchronizedMap(
                new HashMap<String, PreHostService>());
        this.hostServices = synchronizedMap(
                new LinkedHashMap<String, List<HostService>>());
        this.availableScriptServices = synchronizedMap(
                new HashMap<String, HostServiceScriptService>());
    }

    public HostService call(String name) {
        return call(new HashMap<String, Object>(), name);
    }

    public HostService call(Map<String, Object> args, String name) {
        HostServiceService service = availableServices.get(name);
        checkService(name, service);
        Map<String, Object> a = parseArgs(args, name);
        HostService hostService = service.create(a);
        setupTargets(hostService);
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
        return getTargets(name);
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

    private Map<String, Object> parseArgs(Map<String, Object> args,
            String name) {
        Map<String, Object> result = new HashMap<>(args);
        if (!name.equals("ssh")) {
            List<SshHost> targets = parseTarget(args);
            result.put("targets", targets);
        } else {

        }
        return unmodifiableMap(result);
    }

    private List<SshHost> parseTarget(Map<String, Object> args) {
        Object object = args.get("target");
        if (object instanceof SshHost) {
            SshHost host = (SshHost) object;
            return Arrays.asList(host);
        }
        if (object instanceof Ssh) {
            Ssh ssh = (Ssh) object;
            return getTargets(ssh);
        }
        if (object != null) {
            String name = object.toString();
            return getTargets(name);
        } else {
            return getTargets(DEFAULT_TARGETS_NAME);
        }
    }

    private List<SshHost> getTargets(Ssh ssh) {
        try {
            return targets.getHosts(ssh);
        } catch (AssertionError e) {
            throw new NoTargetsForServiceException(e, ssh.getGroup());
        }
    }

    private List<SshHost> getTargets(String name) {
        try {
            return targets.getHosts(name);
        } catch (AssertionError e) {
            throw new NoTargetsForServiceException(e, name);
        }
    }

    private void checkService(String name, HostServiceService service) {
        if (service == null) {
            throw new NullPointerException(
                    format("Service '%s' not found.", name));
        }
    }

    private void setupTargets(HostService service) {
        if (service instanceof Ssh) {
            Ssh ssh = (Ssh) service;
            targets.addTarget(ssh);
        }
    }

}
