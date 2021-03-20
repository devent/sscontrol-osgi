/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.properties.internal;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.propertiesutils.TypedAllPropertiesFactory;
import com.anrisoftware.propertiesutils.TypedAllPropertiesService;
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.google.inject.AbstractModule;

/**
 * Properties service.
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
@Component(service = HostServicePropertiesService.class)
public class HostServicePropertiesServiceImpl implements HostServicePropertiesService {

    @Inject
    private HostServicePropertiesImplFactory factory;

    @Reference
    private TypedAllPropertiesService typedAllPropertiesService;

    @Override
    public HostServiceProperties create() {
        return factory.create();
    }

    @Activate
    protected void start() {
        createInjector(new PropertiesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TypedAllPropertiesFactory.class).toProvider(of(typedAllPropertiesService));
            }
        }).injectMembers(this);
    }
}
