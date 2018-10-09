package com.anrisoftware.sscontrol.hosts.service.internal;

import com.anrisoftware.sscontrol.hosts.service.internal.HostsPreScriptImpl.HostsPreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostsPreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PreHost.class, HostsPreScriptImpl.class)
                .build(HostsPreScriptImplFactory.class));
    }

}
