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
package com.anrisoftware.sscontrol.rkt.service.internal;

import static com.anrisoftware.sscontrol.rkt.service.internal.RktServiceImpl.SERVICE_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.rkt.service.external.Rkt;
import com.anrisoftware.sscontrol.rkt.service.external.RktService;
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Rkt service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RktImpl implements Rkt {

    /**
     *
     * @author Erwin Müller, erwin.mueller@deventm.de
     * @since 1.0
     */
    public interface RktImplFactory extends RktService {

    }

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private String version;

    @AssistedInject
    RktImpl(HostPropertiesService propertiesService,
            @Assisted Map<String, Object> args) {
        this.targets = new ArrayList<TargetHost>();
        this.serviceProperties = propertiesService.create();
        parseArgs(args);
    }

    @Override
    public String getName() {
        return format("%s-%s", SERVICE_NAME, getVersion());
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

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName())
                .append("hosts", targets).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseTargets(args);
        parseVersion(args);
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        setVersion(v.toString());
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

}
