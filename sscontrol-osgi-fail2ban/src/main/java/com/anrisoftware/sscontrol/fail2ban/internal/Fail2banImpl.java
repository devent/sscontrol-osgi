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
package com.anrisoftware.sscontrol.fail2ban.internal;

import static com.anrisoftware.sscontrol.fail2ban.internal.Fail2banServiceImpl.HOSTS_NAME;
import static com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.stringListStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.fail2ban.external.Fail2ban;
import com.anrisoftware.sscontrol.fail2ban.external.Fail2banService;
import com.anrisoftware.sscontrol.fail2ban.external.Jail;
import com.anrisoftware.sscontrol.fail2ban.internal.JailImpl.JailImplFactory;
import com.anrisoftware.sscontrol.types.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.external.SshHost;
import com.anrisoftware.sscontrol.types.external.StringListPropertyUtil.ListProperty;
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

    private final List<SshHost> targets;

    private final JailImplFactory hostFactory;

    private Jail defaultJail;

    @AssistedInject
    Fail2banImpl(Fail2banImplLogger log, JailImplFactory hostFactory,
            HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.hostFactory = hostFactory;
        this.targets = new ArrayList<>();
        this.jails = new ArrayList<>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    @Override
    public String getName() {
        return HOSTS_NAME;
    }

    @Override
    public List<Jail> getJails() {
        return jails;
    }

    @Override
    public SshHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<SshHost> getTargets() {
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
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hosts", jails).append("targets", targets).toString();
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<SshHost>) v);
        }
        if (args.get("ip") != null) {
            ip(args);
        }
    }

}
