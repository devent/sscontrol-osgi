/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
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
