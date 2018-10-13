package com.anrisoftware.sscontrol.services.internal.host;

/*-
 * #%L
 * sscontrol-osgi - services-repository
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.google.inject.Guice.createInjector;
import static com.google.inject.util.Providers.of;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.anrisoftware.sscontrol.types.host.external.HostServicesService;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService;
import com.google.inject.AbstractModule;

/**
 * Creates the host services repository.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Component(service = HostServicesService.class)
public class HostServicesServiceImpl implements HostServicesService {

    @Reference
    private TargetsService targetsService;

    @Inject
    private HostServicesImplFactory repositoryFactory;

    private HostServices scriptsRepository;

    @Override
    public synchronized HostServices create() {
        if (scriptsRepository == null) {
            this.scriptsRepository = repositoryFactory.create();
        }
        return scriptsRepository;
    }

    @Activate
    protected void start() {
        createInjector(new HostServicesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(TargetsService.class).toProvider(of(targetsService));
            }
        }).injectMembers(this);
    }
}
