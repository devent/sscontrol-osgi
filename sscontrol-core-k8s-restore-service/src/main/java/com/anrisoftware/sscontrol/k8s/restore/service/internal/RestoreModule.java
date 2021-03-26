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
package com.anrisoftware.sscontrol.k8s.restore.service.internal;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.ClientImpl.ClientImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.DirSourceImpl.DirSourceImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImpl.RestoreImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.ServiceImpl.ServiceImplFactory;
import com.anrisoftware.sscontrol.k8s.restore.service.internal.SourceImpl.SourceImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class RestoreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, RestoreImpl.class)
                .build(RestoreImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Service.class, ServiceImpl.class)
                .build(ServiceImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Destination.class, DirSourceImpl.class)
                .build(DirSourceImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Client.class, ClientImpl.class)
                .build(ClientImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Source.class, SourceImpl.class)
                .build(SourceImplFactory.class));
    }

}
