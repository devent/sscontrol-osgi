package com.anrisoftware.sscontrol.nfs.service.internal;

import com.anrisoftware.sscontrol.nfs.service.external.Export;
import com.anrisoftware.sscontrol.nfs.service.external.Host;
import com.anrisoftware.sscontrol.nfs.service.internal.ExportImpl.ExportImplFactory;
import com.anrisoftware.sscontrol.nfs.service.internal.HostImpl.HostImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class NfsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HostService.class, NfsImpl.class).build(NfsImplFactory.class));
        install(new FactoryModuleBuilder().implement(Export.class, ExportImpl.class).build(ExportImplFactory.class));
        install(new FactoryModuleBuilder().implement(Host.class, HostImpl.class).build(HostImplFactory.class));
    }

}
