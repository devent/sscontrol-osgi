package com.anrisoftware.sscontrol.hosts.service.internal;

import com.anrisoftware.sscontrol.hosts.service.external.Host;
import com.anrisoftware.sscontrol.hosts.service.internal.HostImpl.HostImplFactory;
import com.anrisoftware.sscontrol.hosts.service.internal.HostsImpl.HostsImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, HostsImpl.class)
                .build(HostsImplFactory.class));
        install(new FactoryModuleBuilder().implement(Host.class, HostImpl.class)
                .build(HostImplFactory.class));
    }

}
