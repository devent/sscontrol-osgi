package com.anrisoftware.sscontrol.services.internal.host;

import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory;
import com.anrisoftware.sscontrol.services.internal.host.ScriptsMap.ScriptsMapFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServices;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class HostServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServices.class, HostServicesImpl.class)
                .build(HostServicesImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ScriptsMap.class, ScriptsMap.class)
                .build(ScriptsMapFactory.class));
    }

}
