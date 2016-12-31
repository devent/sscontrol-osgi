package com.anrisoftware.sscontrol.hostname.debian_8.internal;

import com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8.Hostname_Debian_8_Factory;
import com.anrisoftware.sscontrol.types.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Hostname_Debian_8_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Hostname_Debian_8.class)
                .build(Hostname_Debian_8_Factory.class));
    }

}
