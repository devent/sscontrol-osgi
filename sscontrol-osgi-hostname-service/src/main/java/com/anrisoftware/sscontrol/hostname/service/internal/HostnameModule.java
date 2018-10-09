package com.anrisoftware.sscontrol.hostname.service.internal;

import com.anrisoftware.sscontrol.hostname.service.internal.HostnameImpl.HostnameImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostnameModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, HostnameImpl.class)
                .build(HostnameImplFactory.class));
    }

}
