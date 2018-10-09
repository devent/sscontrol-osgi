package com.anrisoftware.sscontrol.command.shell.internal.cmd;

import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgs;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs;
import com.anrisoftware.sscontrol.command.shell.external.ssh.CmdArgs.CmdArgsFactory;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs.SshArgsFactory;
import com.anrisoftware.sscontrol.command.shell.internal.cmd.SshOptions.SshOptionsFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class CmdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(CmdArgs.class, CmdArgsImpl.class)
                .build(CmdArgsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SshOptions.class, SshOptions.class)
                .build(SshOptionsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(SshArgs.class, SshArgsImpl.class)
                .build(SshArgsFactory.class));
    }

}
