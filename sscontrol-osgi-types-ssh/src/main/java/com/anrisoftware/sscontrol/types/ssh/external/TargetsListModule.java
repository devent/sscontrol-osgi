package com.anrisoftware.sscontrol.types.ssh.external;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class TargetsListModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(TargetsList.class, TargetsList.class)
                .build(TargetsListFactory.class));
        install(new FactoryModuleBuilder()
                .implement(TargetsAddressList.class, TargetsAddressList.class)
                .build(TargetsAddressListFactory.class));
    }

}
