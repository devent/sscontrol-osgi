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
