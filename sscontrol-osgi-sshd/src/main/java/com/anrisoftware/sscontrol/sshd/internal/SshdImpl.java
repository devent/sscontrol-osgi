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
package com.anrisoftware.sscontrol.sshd.internal;

import static com.anrisoftware.sscontrol.sshd.internal.SshdServiceImpl.SSHD_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static org.codehaus.groovy.runtime.InvokerHelper.invokeMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.debug.external.DebugService;
import com.anrisoftware.sscontrol.sshd.external.Sshd;
import com.anrisoftware.sscontrol.sshd.external.SshdService;
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
public class SshdImpl implements Sshd {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface SshdImplFactory extends SshdService {

    }

    private final SshdImplLogger log;

    private final HostServiceProperties serviceProperties;

    private final List<TargetHost> targets;

    private final List<String> users;

    private DebugLogging debug;

    @AssistedInject
    SshdImpl(SshdImplLogger log, HostServicePropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.serviceProperties = propertiesService.create();
        this.targets = new ArrayList<TargetHost>();
        this.users = new ArrayList<String>();
        parseArgs(args);
    }

    @Inject
    public void setDebugService(DebugService debugService) {
        this.debug = debugService.create();
    }

    @Override
    public String getName() {
        return SSHD_NAME;
    }

    public void debug(Map<String, Object> args, String name) {
        Map<String, Object> arguments = new HashMap<String, Object>(args);
        arguments.put("name", name);
        invokeMethod(debug, "debug", arguments);
    }

    public void debug(Map<String, Object> args) {
        Map<String, Object> a = new HashMap<String, Object>(args);
        if (a.get("name") == null) {
            a.put("name", "debug");
        }
        invokeMethod(debug, "debug", a);
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    public List<String> getUser() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                log.addUser(SshdImpl.this, property);
                users.add(property);
            }
        });
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return targets;
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
    public List<String> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("targets", targets).append("debug", debug)
                .append("users", users).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

}
