package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Collectd_Debian_9_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Collectd_Debian_9.class)
                .build(Collectd_Debian_9_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Collectd_5_7_Debian_9.class)
                .build(Collectd_5_7_Debian_9_Factory.class));
    }

}
