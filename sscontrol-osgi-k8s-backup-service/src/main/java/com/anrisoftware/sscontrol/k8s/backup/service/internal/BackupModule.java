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
package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImpl.BackupImplFactory;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.ClientImpl.ClientImplFactory;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.DirDestinationImpl.DirDestinationImplFactory;
import com.anrisoftware.sscontrol.k8s.backup.service.internal.ServiceImpl.ServiceImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class BackupModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, BackupImpl.class)
                .build(BackupImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Service.class, ServiceImpl.class)
                .build(ServiceImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Destination.class, DirDestinationImpl.class)
                .build(DirDestinationImplFactory.class));
        install(new FactoryModuleBuilder().implement(Client.class, ClientImpl.class)
                .build(ClientImplFactory.class));
    }

}
