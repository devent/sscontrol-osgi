package com.anrisoftware.sscontrol.hostname.service.internal;

import com.anrisoftware.sscontrol.hostname.service.internal.HostnamePreScriptImpl.HostnamePreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HostnamePreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PreHost.class, HostnamePreScriptImpl.class)
                .build(HostnamePreScriptImplFactory.class));
    }

}
