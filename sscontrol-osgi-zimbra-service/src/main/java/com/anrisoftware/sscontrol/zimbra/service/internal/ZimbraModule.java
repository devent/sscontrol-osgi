package com.anrisoftware.sscontrol.zimbra.service.internal;

import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.zimbra.service.external.Domain;
import com.anrisoftware.sscontrol.zimbra.service.internal.DomainImpl.DomainImplFactory;
import com.anrisoftware.sscontrol.zimbra.service.internal.ZimbraImpl.ZimbraImplFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class ZimbraModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, ZimbraImpl.class)
                .build(ZimbraImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Domain.class, DomainImpl.class)
                .build(DomainImplFactory.class));
    }

}
