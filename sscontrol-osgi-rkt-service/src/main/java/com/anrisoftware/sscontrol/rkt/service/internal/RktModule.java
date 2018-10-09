package com.anrisoftware.sscontrol.rkt.service.internal;

import com.anrisoftware.sscontrol.rkt.service.internal.RktImpl.RktImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class RktModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, RktImpl.class)
                .build(RktImplFactory.class));
    }

}
