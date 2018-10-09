package com.anrisoftware.sscontrol.fail2ban.service.internal;

import com.anrisoftware.sscontrol.fail2ban.service.internal.Fail2banPreScriptImpl.HostnamePreScriptImplFactory;
import com.anrisoftware.sscontrol.types.host.external.PreHost;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class Fail2banPreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(PreHost.class, Fail2banPreScriptImpl.class)
                .build(HostnamePreScriptImplFactory.class));
    }

}
