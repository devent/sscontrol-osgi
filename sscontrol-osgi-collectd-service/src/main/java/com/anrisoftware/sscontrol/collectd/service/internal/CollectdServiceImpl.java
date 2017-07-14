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
package com.anrisoftware.sscontrol.collectd.service.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import java.util.Map;

import javax.inject.Inject;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.anrisoftware.sscontrol.collectd.service.external.Collectd;
import com.anrisoftware.sscontrol.collectd.service.external.CollectdService;
import com.anrisoftware.sscontrol.collectd.service.internal.CollectdImpl.CollectdImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.google.inject.AbstractModule;

/**
 * Creates the collectd service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component
@Service(HostServiceService.class)
public class CollectdServiceImpl implements CollectdService {

    static final String SERVICE_NAME = "collectd";

    @Property(value = SERVICE_NAME)
    static final String NAME_PROPERTY = "service.name";

    private String name;

    @Reference
    private HostServicesService hostServicesService;

    @Reference
    private TargetsService targetsService;

    @Reference
    private HostPropertiesService hostPropertiesService;

    @Inject
    private CollectdImplFactory serviceFactory;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collectd create(Map<String, Object> args) {
        return (Collectd) serviceFactory.create(args);
    }

    @Activate
    public void start() {
        createInjector(new CollectdModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(HostServicesService.class)
                        .toProvider(of(hostServicesService));
                bind(TargetsService.class).toProvider(of(targetsService));
                bind(HostPropertiesService.class)
                        .toProvider(of(hostPropertiesService));
            }
        }).injectMembers(this);
    }
}