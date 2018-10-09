package com.anrisoftware.sscontrol.hosts.script.linux.internal;

import com.anrisoftware.sscontrol.hosts.script.linux.external.Hosts_Linux_Factory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Hosts_Linux_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Hosts_Linux.class)
                .build(Hosts_Linux_Factory.class));
    }

}
