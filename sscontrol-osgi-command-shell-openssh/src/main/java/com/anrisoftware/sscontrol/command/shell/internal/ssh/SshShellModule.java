package com.anrisoftware.sscontrol.command.shell.internal.ssh;

import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshMaster.SshMasterFactory;
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshRun.SshRunFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshShellModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(SshRun.class, SshRun.class)
                .build(SshRunFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SshMaster.class, SshMaster.class)
                .build(SshMasterFactory.class));
    }

}
