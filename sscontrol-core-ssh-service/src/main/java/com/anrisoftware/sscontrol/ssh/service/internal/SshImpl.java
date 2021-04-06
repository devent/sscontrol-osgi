/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.ssh.service.internal;

import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.globalpom.core.resources.ToURI;
import com.anrisoftware.sscontrol.debug.external.DebugLoggingService;
import com.anrisoftware.sscontrol.ssh.service.external.SshService;
import com.anrisoftware.sscontrol.ssh.service.internal.SshHostImpl.SshHostImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.anrisoftware.sscontrol.types.ssh.external.Ssh;
import com.anrisoftware.sscontrol.types.ssh.external.SshHost;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * <i>Ssh</i> script service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class SshImpl implements Ssh {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface SshImplFactory extends SshService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final List<SshHost> hosts;

    private transient SshImplLogger log;

    private transient SshHostImplFactory hostFactory;

    private DebugLogging debug;

    private String group;

    private String system;

    private URI defaultKey;

    private Object socket;

    @AssistedInject
    SshImpl(SshImplLogger log, HostServicePropertiesService propertiesService, SshHostImplFactory hostFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<>();
        this.hosts = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.hostFactory = hostFactory;
        this.defaultKey = null;
        parseArgs(args);
    }

    @Inject
    public void setDebugService(DebugLoggingService debugService) {
        this.debug = debugService.create();
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    public void target(Map<String, Object> args) {
        Object v = args.get("target");
        @SuppressWarnings("unchecked")
        List<SshHost> l = InvokerHelper.asList(v);
        targets.addAll(l);
    }

    public void group(String group) {
        setGroup(group);
    }

    public void setGroup(String group) {
        this.group = group;
        log.groupSet(this, group);
    }

    @Override
    public String getGroup() {
        if (StringUtils.isEmpty(group)) {
            return "default";
        } else {
            return group;
        }
    }

    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<>(args);
        arguments.put("name", name);
        invokeMethod(debug, "debug", arguments);
    }

    public void debug(Map<String, Object> args) {
        Map<String, Object> arguments = new HashMap<>(args);
        invokeMethod(debug, "debug", arguments);
    }

    public void host(String host) {
        Map<String, Object> a = new HashMap<>();
        a.put("host", host);
        host(a);
    }

    public void host(Map<String, Object> args, String host) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("host", host);
        host(a);
    }

    public void host(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        if (a.get("key") == null) {
            a.put("key", defaultKey);
        }
        addHost(a);
    }

    public List<String> getHost() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                host(property);
            }
        });
    }

    @Override
    public List<SshHost> getHosts() {
        return hosts;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDebug() {
        return (List<Object>) invokeMethod(debug, "getDebug", null);
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
        return "ssh";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName()).append("group", group).append("hosts", hosts.size())
                .toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
        v = args.get("group");
        if (v != null) {
            setGroup(v.toString());
        } else {
            setGroup("default");
        }
        v = args.get("system");
        if (v != null) {
            this.system = v.toString();
        }
        v = args.get("key");
        if (v != null) {
            this.defaultKey = ToURI.toURI(v).convert();
        }
        v = args.get("socket");
        if (v != null) {
            this.socket = v;
        }
        v = args.get("host");
        if (v != null) {
            host(args);
        }
    }

    private void addHost(Map<String, Object> args) {
        Object v = args.get("system");
        if (v == null) {
            args.put("system", system);
        }
        v = args.get("socket");
        if (v == null) {
            args.put("socket", socket);
        }
        SshHost host = hostFactory.create(args);
        this.hosts.add(host);
        this.targets.add(host);
        log.hostAdded(this, host);
    }

}
