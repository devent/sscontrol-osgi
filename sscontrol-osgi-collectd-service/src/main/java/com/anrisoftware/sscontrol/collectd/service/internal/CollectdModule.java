package com.anrisoftware.sscontrol.collectd.service.internal;

import com.anrisoftware.sscontrol.collectd.service.external.Config;
import com.anrisoftware.sscontrol.collectd.service.internal.CollectdImpl.CollectdImplFactory;
import com.anrisoftware.sscontrol.collectd.service.internal.ConfigImpl.ConfigImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CollectdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostService.class, CollectdImpl.class)
                .build(CollectdImplFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Config.class, ConfigImpl.class)
                .build(ConfigImplFactory.class));
    }

}
