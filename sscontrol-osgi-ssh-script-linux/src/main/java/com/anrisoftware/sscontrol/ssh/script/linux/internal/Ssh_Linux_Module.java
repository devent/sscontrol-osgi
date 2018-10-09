package com.anrisoftware.sscontrol.ssh.script.linux.internal;

import com.anrisoftware.sscontrol.ssh.script.linux.external.Ssh_Linux_Factory;
import com.anrisoftware.sscontrol.ssh.script.linux.internal.SshLinux;
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class Ssh_Linux_Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HostServiceScript.class, SshLinux.class)
                .build(Ssh_Linux_Factory.class));
    }

}
