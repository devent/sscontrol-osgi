/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.etcd.internal;

import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.etcd.external.Binding;
import com.anrisoftware.sscontrol.etcd.external.Binding.BindingFactory;
import com.anrisoftware.sscontrol.etcd.external.Etcd;
import com.anrisoftware.sscontrol.etcd.external.EtcdService;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Etcd</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class EtcdImpl implements Etcd {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface EtcdImplFactory extends EtcdService {

    }

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    private final EtcdImplLogger log;

    private DebugLogging debug;

    private final List<Binding> bindings;

    private final List<Binding> advertises;

    private final BindingFactory bindingFactory;

    private String member;

    @Inject
    EtcdImpl(EtcdImplLogger log, HostPropertiesService propertiesService,
            BindingFactory bindingFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.bindings = new ArrayList<>();
        this.advertises = new ArrayList<>();
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
     * bind scheme: "http", address: "127.0.0.1", port: 8080
     * </pre>
     */
    public void bind(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        Binding binding = bindingFactory.create(a);
        log.bindingAdded(this, binding);
        bindings.add(binding);
    }

    /**
     * <pre>
     * advertise scheme: "http", address: "127.0.0.1", port: 8080
     * </pre>
     */
    public void advertise(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        Binding binding = bindingFactory.create(a);
        log.advertiseAdded(this, binding);
        advertises.add(binding);
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
        return "etcd";
    }

    public void setMemberName(String member) {
        this.member = member;
        log.memberNameSet(this, member);
    }

    @Override
    public String getMemberName() {
        return member;
    }

    @Override
    public List<Binding> getBindings() {
        return bindings;
    }

    @Override
    public List<Binding> getAdvertises() {
        return advertises;
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
        v = args.get("member");
        if (v != null) {
            setMemberName(v.toString());
        }
    }

}
