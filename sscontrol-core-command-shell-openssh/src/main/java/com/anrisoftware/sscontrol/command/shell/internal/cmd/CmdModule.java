/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class CmdModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(CmdArgs.class, CmdArgsImpl.class).build(CmdArgsFactory.class));
        install(new FactoryModuleBuilder().implement(SshOptions.class, SshOptions.class)
                .build(SshOptionsFactory.class));
        install(new FactoryModuleBuilder().implement(SshArgs.class, SshArgsImpl.class).build(SshArgsFactory.class));
    }

}
