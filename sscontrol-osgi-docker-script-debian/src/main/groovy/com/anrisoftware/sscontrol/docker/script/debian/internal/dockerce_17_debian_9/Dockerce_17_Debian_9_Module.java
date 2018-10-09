package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Dockerce_17_Debian_9_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Dockerce_17_Debian_9.class)
                .build(Dockerce_17_Debian_9_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Dockerce_17_Systemd_Debian_9.class)
                .build(Dockerce_17_Systemd_Debian_9_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class,
                        Dockerce_17_Upstream_Debian_9.class)
                .build(Dockerce_17_Upstream_Debian_9_Factory.class));
    }

}
