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
package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Backend;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Backend.BackendFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Binding;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Etcd;
import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDocker;
import com.anrisoftware.sscontrol.flanneldocker.service.external.FlannelDockerService;
import com.anrisoftware.sscontrol.flanneldocker.service.external.Network;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.BindingImpl.BindingImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImpl.EtcdImplFactory;
import com.anrisoftware.sscontrol.flanneldocker.service.internal.NetworkImpl.NetworkImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil;
import com.anrisoftware.sscontrol.types.misc.external.GeneticListPropertyUtil.GeneticListProperty;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class FlannelDockerImpl implements FlannelDocker {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface FlannelDockerImplFactory extends FlannelDockerService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final FlannelDockerImplLogger log;

    private DebugLogging debug;

    private Etcd etcd;

    private final EtcdImplFactory etcdFactory;

    private Network network;

    private final NetworkImplFactory networkFactory;

    @Inject
    private Map<String, BackendFactory> backendFactories;

    private Backend backend;

    private Binding binding;

    private final BindingImplFactory bindingFactory;

    private final List<Object> nodes;

    @Inject
    FlannelDockerImpl(FlannelDockerImplLogger log,
            HostServicePropertiesService propertiesService,
            EtcdImplFactory etcdFactory, NetworkImplFactory networkFactory,
            BindingImplFactory bindingFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.etcd = etcdFactory.create();
        this.etcdFactory = etcdFactory;
        this.network = networkFactory.create();
        this.networkFactory = networkFactory;
        this.binding = null;
        this.bindingFactory = bindingFactory;
        this.nodes = new ArrayList<>();
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
        List<TargetHost> l = InvokerHelper.asList(v);
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
     * bind "127.0.0.1" // or
     * bind "eth0"
     * </pre>
     */
    public void bind(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        bind(a);
    }

    /**
     * <pre>
     * bind address: "127.0.0.1" // or
     * bind name: "eth0"
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        this.binding = bindingFactory.create(a);
        log.bindingSet(this, binding);
    }

    /**
     * <pre>
     * etcd "http://127.0.0.1:2379"
     * </pre>
     */
    public Etcd etcd(String address) {
        Map<String, Object> args = new HashMap<>();
        args.put("endpoints", address);
        return etcd(args);
    }

    /**
     * <pre>
     * etcd endpoints: "http://127.0.0.1:2379", prefix: "/atomic.io/network"
     * </pre>
     */
    public Etcd etcd(Map<String, Object> args) {
        this.etcd = etcdFactory.create(args);
        log.etcdSet(this, etcd);
        return etcd;
    }

    /**
     * <pre>
     * network "10.2.0.0/16"
     * </pre>
     */
    public Network network(String address) {
        Map<String, Object> args = new HashMap<>();
        args.put("address", address);
        this.network = networkFactory.create(args);
        log.networkSet(this, network);
        return network;
    }

    /**
     * <pre>
     * backend "vxlan"
     * </pre>
     */
    public Backend backend(String type) {
        Map<String, Object> args = new HashMap<>();
        BackendFactory factory = backendFactories.get(type);
        assertThat(format("backend(%s)=null", type), factory,
                is(notNullValue()));
        this.backend = factory.create(args);
        log.backendSet(this, backend);
        return backend;
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

    @Override
    public DebugLogging getDebugLogging() {
        return debug;
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
    public String getName() {
        return "flannel-docker";
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public Etcd getEtcd() {
        return etcd;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Backend getBackend() {
        return backend;
    }

    @Override
    public List<Object> getNodes() {
        return nodes;
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
            targets.addAll((List<TargetHost>) v);
        }
    }

}
