package com.anrisoftware.sscontrol.utils.systemd.external;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SystemdUtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(SystemdUtils.class, SystemdUtils.class)
                .build(SystemdUtilsFactory.class));
    }

}
