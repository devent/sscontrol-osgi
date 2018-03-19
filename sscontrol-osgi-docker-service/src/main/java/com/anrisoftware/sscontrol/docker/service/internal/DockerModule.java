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
package com.anrisoftware.sscontrol.docker.service.internal;

import com.anrisoftware.sscontrol.docker.service.external.LoggingDriver;
import com.anrisoftware.sscontrol.docker.service.external.Mirror;
import com.anrisoftware.sscontrol.docker.service.external.Registry;
import com.anrisoftware.sscontrol.docker.service.internal.DockerImpl.DockerImplFactory;
import com.anrisoftware.sscontrol.docker.service.internal.LoggingDriverImpl.LoggingDriverImplFactory;
import com.anrisoftware.sscontrol.docker.service.internal.MirrorImpl.MirrorImplFactory;
import com.anrisoftware.sscontrol.docker.service.internal.RegistryImpl.RegistryImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Docker</i> script module.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class DockerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, DockerImpl.class)
                .build(DockerImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Registry.class, RegistryImpl.class)
                .build(RegistryImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Mirror.class, MirrorImpl.class)
                .build(MirrorImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(LoggingDriver.class, LoggingDriverImpl.class)
                .build(LoggingDriverImplFactory.class));
    }
}
