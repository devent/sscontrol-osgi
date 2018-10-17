/*-
 * #%L
 * sscontrol-osgi - ssh-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.ssh.script.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.CmdExecHelper.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.command.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.command.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.st.StModule
import com.anrisoftware.sscontrol.command.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdExecHelperModule
import com.anrisoftware.sscontrol.ssh.script.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@EnabledIfSystemProperty(named = SshScript_Debian_9_Test.LOCAL_PROFILE, matches = "true")
class SshScript_Debian_9_Test extends AbstractScriptTestBase {

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory sshLinuxFactory

    static Map expectedResources = [:]

    @Test
    void "cat_release"() {
        def test = [
            name: "cat_release",
            input: """
service "ssh", host: "localhost"
""",
            expected: { Map args ->
                HostServices services = args.services
                def targets = services.targets.getHosts 'default'
                assert targets[0].system.name == 'debian'
                assert targets[0].system.version == '9'
            },
        ]
        doTest test
    }

    @Test
    void "socket"() {
        def socketFile = createSshSocket injector, host: "localhost"
        def test = [
            name: "socket",
            input: """
service "ssh", host: "localhost", socket: "$socketFile"
""",
            expected: { Map args ->
                HostServices services = args.services
                def targets = services.targets.getHosts 'default'
                assert targets[0].socket == socketFile
            },
        ]
        doTest test
    }

    String getServiceName() {
        'ssh'
    }

    String getScriptServiceName() {
        'ssh/linux/0'
    }

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'cp',
            'rm',
            'sudo',
            'scp',
            'id',
        ]
    }

    void putSshService(HostServices services) {
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', sshLinuxFactory
    }

    List getAdditionalModules() {
        [
            new SshModule(),
            new Ssh_Linux_Module(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TemplateModule(),
            new FactsModule(),
            new TokensTemplateModule(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new TemplateResModule(),
            new SystemNameMappingsModule(),
            new StModule(),
            new CmdExecHelperModule(),
            new CmdImplModule(),
        ]
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
