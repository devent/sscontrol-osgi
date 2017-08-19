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
package com.anrisoftware.sscontrol.fail2ban.service.internal;

import static com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banServiceImpl.FAIL2BAN_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.fail2ban.service.external.Fail2ban;
import com.anrisoftware.sscontrol.fail2ban.service.external.Fail2banService;
import com.anrisoftware.sscontrol.fail2ban.service.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.service.internal.JailImpl.JailImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Hosts service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class Fail2banImpl implements Fail2ban {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface Fail2banImplFactory extends Fail2banService {

    }

    private final Fail2banImplLogger log;

    private final List<Jail> jails;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final JailImplFactory jailFactory;

    private Jail defaultJail;

    private DebugLogging debug;

    @AssistedInject
    Fail2banImpl(Fail2banImplLogger log, JailImplFactory jailFactory,
            HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.jailFactory = jailFactory;
        this.targets = new ArrayList<>();
        this.jails = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        this.defaultJail = jailFactory.create("DEFAULT");
        parseArgs(args);
    }

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
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

    public void notify(String address) {
        Map<String, Object> a = new HashMap<>();
        a.put("address", address);
        notify(a);
    }

    public void notify(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        invokeMethod(defaultJail, "notify", a);
    }

    public void banning(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        invokeMethod(defaultJail, "banning", a);
    }

    @SuppressWarnings("unchecked")
    public List<String> getIgnore() {
        return (List<String>) InvokerHelper.getProperty(defaultJail, "ignore");
    }

    public void ignore(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<>(args);
        invokeMethod(defaultJail, "ignore", a);
    }

    public Jail jail(String service) {
        return jail(new HashMap<String, Object>(), service);
    }

    public Jail jail(Map<String, Object> args, String service) {
        Map<String, Object> a = new HashMap<>(args);
        a.put("service", service);
        if (a.get("enabled") == null) {
            a.put("enabled", true);
        }
        Jail jail = jailFactory.create(a);
        jails.add(jail);
        log.jailAdded(this, jail);
        return jail;
    }

    @Override
    public String getName() {
        return FAIL2BAN_NAME;
    }

    @Override
    public Jail getDefaultJail() {
        return defaultJail;
    }

    @Override
    public List<Jail> getJails() {
        return jails;
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return targets;
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public DebugLogging getDebugLogging() {
        return debug;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).append("default", defaultJail)
                .append("jails", jails).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
        if (args.get("notify") != null) {
            Map<String, Object> a = new HashMap<>(args);
            a.put("service", "DEFAULT");
            this.defaultJail = jailFactory.create(a);
            log.defaultJailSet(this, defaultJail);
        }
    }

}
