package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7;

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Collectd_Centos_7_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Collectd_Centos_7.class)
                .build(Collectd_Centos_7_Factory.class));
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Collectd_5_7_Centos_7.class)
                .build(Collectd_5_7_Centos_7_Factory.class));
    }

}
