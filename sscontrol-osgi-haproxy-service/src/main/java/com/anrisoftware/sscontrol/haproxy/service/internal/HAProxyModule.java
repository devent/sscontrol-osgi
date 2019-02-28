package com.anrisoftware.sscontrol.haproxy.service.internal;

import com.anrisoftware.sscontrol.haproxy.service.external.Backend;
import com.anrisoftware.sscontrol.haproxy.service.external.Frontend;
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy;
import com.anrisoftware.sscontrol.haproxy.service.internal.BackendImpl.BackendImplFactory;
import com.anrisoftware.sscontrol.haproxy.service.internal.FrontendImpl.FrontendImplFactory;
import com.anrisoftware.sscontrol.haproxy.service.internal.ProxyImpl.ProxyImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class HAProxyModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostService.class, HAProxyImpl.class).build(HAProxyImplFactory.class));
        install(new FactoryModuleBuilder().implement(Proxy.class, ProxyImpl.class).build(ProxyImplFactory.class));
        install(new FactoryModuleBuilder().implement(Frontend.class, FrontendImpl.class).build(FrontendImplFactory.class));
        install(new FactoryModuleBuilder().implement(Backend.class, BackendImpl.class).build(BackendImplFactory.class));
    }

}
