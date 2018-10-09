package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class FlannelDockerDebianModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, FlannelDockerDebian.class)
                .build(FlannelDockerDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        FlannelDockerUpstreamSystemdDebian.class)
                .build(FlannelDockerUpstreamSystemdDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        FlannelDockerUpstreamDebian.class)
                .build(FlannelDockerUpstreamDebianFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, FlannelDockerUfw.class)
                .build(FlannelDockerUfwFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, IperfConnectionCheck.class)
                .build(IperfConnectionCheckFactory.class));
    }

}
