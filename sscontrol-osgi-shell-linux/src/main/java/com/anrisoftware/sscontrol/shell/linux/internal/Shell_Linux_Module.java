package com.anrisoftware.sscontrol.shell.linux.internal;

import com.anrisoftware.sscontrol.shell.linux.external.Shell_Linux_Factory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Shell_Linux_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, Shell_Linux.class)
                .build(Shell_Linux_Factory.class));
    }

}
