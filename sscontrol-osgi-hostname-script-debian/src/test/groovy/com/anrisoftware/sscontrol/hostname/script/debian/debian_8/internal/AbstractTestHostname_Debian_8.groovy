/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname.script.debian.debian_8.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.command.shell.external.Cmd
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.command.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.command.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdImpl
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.command.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.st.StModule
import com.anrisoftware.sscontrol.command.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.hostname.script.debian.internal.debian_8.Hostname_Debian_8_Factory
import com.anrisoftware.sscontrol.hostname.script.debian.internal.debian_8.Hostname_Debian_8_Module
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.AbstractModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTestHostname_Debian_8 extends AbstractScriptTestBase {

    @Inject
    SshImplFactory sshFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    HostnameImplFactory hostnameFactory

    @Inject
    Hostname_Debian_8_Factory hostnameDebianFactory

    String getServiceName() {
        'hostname'
    }

    String getScriptServiceName() {
        'hostname/debian/8'
    }

    void createDummyCommands(File dir) {
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'apt-get',
            'hostnamectl'
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'hostname', hostnameFactory
        services.putAvailableScriptService 'hostname/debian/8', hostnameDebianFactory
    }

    List getAdditionalModules() {
        [
            new HostnameModule(),
            new Hostname_Debian_8_Module(),
            new SshModule(),
            new SshPreModule(),
            new DebugLoggingModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TemplateModule(),
            new TokensTemplateModule(),
            new FactsModule(),
            new TemplateResModule(),
            new SystemNameMappingsModule(),
            new StModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind Cmd to CmdImpl
                }
            }
        ]
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
