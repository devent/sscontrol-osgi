/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.runner.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8_Module
import com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8.Hostname_Debian_8_Factory
import com.anrisoftware.sscontrol.hostname.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.internal.HostnamePreModule
import com.anrisoftware.sscontrol.hostname.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.hostname.internal.HostnamePreScriptImpl.HostnamePreScriptImplFactory
import com.anrisoftware.sscontrol.parser.groovy.internal.ParserModule
import com.anrisoftware.sscontrol.parser.groovy.internal.ParserImpl.ParserImplFactory
import com.anrisoftware.sscontrol.replace.internal.ReplaceModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshPreScriptImpl.SshPreScriptImplFactory
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class RunnerImplTest extends AbstractScriptTestBase {

    @Inject
    ParserImplFactory parserFactory

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    SshPreScriptImplFactory sshPreFactory

    @Inject
    HostnameImplFactory hostnameFactory

    @Inject
    HostnamePreScriptImplFactory hostnamePreFactory

    @Inject
    Hostname_Debian_8_Factory hostname_Debian_8_Factory

    static final URL hostnameScript = RunnerImplTest.class.getResource('HostnameScript.groovy')

    @Test
    void "run scripts"() {
        def testCases = [
            [
                name: "default_target",
                expected: { Map args ->
                    File dir = args.dir
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            doTest test, k
        }
    }

    void doTest(Map test, int k) {
        log.info '\n######### {}. case: {}', k, test
        File parent = folder.newFolder()
        File scriptFile = new File(parent, "Script.groovy")
        File dir = folder.newFolder()
        IOUtils.copy hostnameScript.openStream(), new FileOutputStream(scriptFile)
        def roots = [parent.toURI()] as URI[]
        def variables = [:]
        def services = putServices(servicesFactory.create())
        def parser = parserFactory.create(roots, "Script.groovy", variables, services)
        def parsed = parser.parse()
        assert parsed.services.size() == 2
        runnerFactory.create(threads, parsed).run variables
    }

    String getServiceName() {
    }

    String getScriptServiceName() {
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailablePreService 'ssh', sshPreFactory
        services.putAvailableService 'hostname', hostnameFactory
        services.putAvailablePreService 'hostname', hostnamePreFactory
        services.putAvailableScriptService 'hostname', hostname_Debian_8_Factory
        return services
    }


    void createDummyCommands(File dir) {
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service'
        ]
    }

    List getAdditionalModules() {
        [
            new ParserModule(),
            new RunnerModule(),
            new HostnameModule(),
            new HostnamePreModule(),
            new Hostname_Debian_8_Module(),
            new SshModule(),
            new SshPreModule(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new SshShellModule(),
            new ShellCmdModule(),
            new CmdImplModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TemplateModule(),
            new TokensTemplateModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
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
