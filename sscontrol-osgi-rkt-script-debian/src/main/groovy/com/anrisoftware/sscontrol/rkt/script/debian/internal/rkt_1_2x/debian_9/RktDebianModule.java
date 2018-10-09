package com.anrisoftware.sscontrol.rkt.script.debian.internal.rkt_1_2x.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class RktDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, RktDebian.class)
                .build(RktDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, RktSystemdDebian.class)
                .build(RktSystemdDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, RktUpstreamDebian.class)
                .build(RktUpstreamDebianFactory.class));
    }

}
