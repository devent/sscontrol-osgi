package com.anrisoftware.sscontrol.flanneldocker.script.upstream.external;

import com.anrisoftware.sscontrol.types.ssh.external.TargetsAddressList;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsAddressListFactory;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsList;
import com.anrisoftware.sscontrol.types.ssh.external.TargetsListFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FlannelDockerUpstreamModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(TargetsAddressList.class, TargetsAddressList.class)
                .build(TargetsAddressListFactory.class));
        install(new FactoryModuleBuilder()
                .implement(TargetsList.class, TargetsList.class)
                .build(TargetsListFactory.class));
    }

}
