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
package com.anrisoftware.sscontrol.docker.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.docker.external.Docker;
import com.anrisoftware.sscontrol.docker.external.DockerService;
import com.anrisoftware.sscontrol.docker.external.Registry;
import com.anrisoftware.sscontrol.docker.internal.RegistryImpl.RegistryImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Docker</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class DockerImpl implements Docker {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface DockerImplFactory extends DockerService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final DockerImplLogger log;

    private final List<String> cgroups;

    private final RegistryImplFactory registryFactory;

    private Registry registry;

    @Inject
    DockerImpl(DockerImplLogger log, HostPropertiesService propertiesService,
            RegistryImplFactory registryFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.cgroups = new ArrayList<>();
        this.registryFactory = registryFactory;
        this.registry = registryFactory.create();
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
     * groups << 'memory'
     * </pre>
     */
    public List<String> getGroups() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                cgroups.add(property);
                log.cgroupAdded(DockerImpl.this, property);
            }
        });
    }

    /**
     * <pre>
     * registry mirror: 'host'
     * </pre>
     */
    public Registry registry(Map<String, Object> args) {
        this.registry = registryFactory.create(args);
        log.registrySet(this, registry);
        return registry;
    }

    public void addTargets(List<TargetHost> list) {
        targets.addAll(new ArrayList<>(list));
        log.targetsAdded(this, list);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
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
    public List<String> getCgroups() {
        return cgroups;
    }

    @Override
    public String getName() {
        return "docker";
    }

    @Override
    public Registry getRegistry() {
        return registry;
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
            addTargets((List<TargetHost>) v);
        }
    }

}
