package com.anrisoftware.sscontrol.utils.ufw.linux.external;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class UfwUtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(UfwUtils.class, UfwLinuxUtils.class)
                .build(UfwLinuxUtilsFactory.class));
    }

}
