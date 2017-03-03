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
package com.anrisoftware.sscontrol.k8snode.internal;

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
import com.anrisoftware.sscontrol.k8snode.external.K8sNode;
import com.anrisoftware.sscontrol.k8snode.external.K8sNodeService;
import com.anrisoftware.sscontrol.k8snode.external.Master;
import com.anrisoftware.sscontrol.k8snode.internal.MasterImpl.MasterImplFactory;
import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.tls.external.Tls.TlsFactory;
import com.anrisoftware.sscontrol.types.external.DebugLogging;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>K8s-Node</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class K8sNodeImpl implements K8sNode {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface K8sNodeImplFactory extends K8sNodeService {

    }

    private final List<SshHost> targets;

    private final HostServiceProperties serviceProperties;

    private final K8sNodeImplLogger log;

    private DebugLogging debug;

    private Tls tls;

    private transient TlsFactory tlsFactory;

    private Master master;

    private transient MasterImplFactory masterFactory;

    @Inject
    K8sNodeImpl(K8sNodeImplLogger log, HostPropertiesService propertiesService,
            TlsFactory tlsFactory, MasterImplFactory masterFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.tlsFactory = tlsFactory;
        this.tls = tlsFactory.create();
        this.masterFactory = masterFactory;
        this.master = masterFactory.create();
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
     * tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
     * </pre>
     */
    public void tls(Map<String, Object> args) {
        this.tls = tlsFactory.create(args);
        log.tlsSet(this, tls);
    }

    /**
     * <pre>
     * master target: 'master' // or
     * master address: 'http://master'
     * </pre>
     */
    public void master(Map<String, Object> args) {
        this.master = masterFactory.create(args);
        log.masterSet(this, master);
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
        return "k8s-master";
    }

    @Override
    public Tls getTls() {
        return tls;
    }

    @Override
    public Master getMaster() {
        return master;
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
    }

}
