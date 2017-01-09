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
package com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal.Sshd_Debian_8
import com.anrisoftware.sscontrol.replace.internal.ReplaceModule
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.sshd.internal.SshdModule
import com.anrisoftware.sscontrol.sshd.internal.SshdImpl.SshdImplFactory
import com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal.Sshd_Debian_8.Sshd_Debian_8_Factory
import com.anrisoftware.sscontrol.types.external.HostServiceScript
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Sshd_Debian_8_Test extends AbstractScriptTestBase {

    @Inject
    SshdImplFactory sshdFactory

    @Inject
    Sshd_Debian_8_Factory sshdDebianFactory

    @Inject
    CmdRunCaller cmdRunCaller

    static Map expectedResources = [
        default_target_sudo: Sshd_Debian_8_Test.class.getResource('default_target_sudo_expected.txt'),
        default_target_apt_get: Sshd_Debian_8_Test.class.getResource('default_target_apt_get_expected.txt'),
        default_target_service: Sshd_Debian_8_Test.class.getResource('default_target_service_expected.txt'),
    ]

    @Test
    void "sshd script"() {
        def testCases = [
            [
                name: "default_target",
                input: """
service "sshd"
""",
                expected: { Map args ->
                    File dir = args.dir
                    assertStringContent fileToString(new File(dir, 'sudo.out')), resourceToString(expectedResources["${args.test.name}_sudo"])
                    assertStringContent fileToString(new File(dir, 'apt-get.out')), resourceToString(expectedResources["${args.test.name}_apt_get"])
                    assertStringContent fileToString(new File(dir, 'service.out')), resourceToString(expectedResources["${args.test.name}_service"])
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            doTest test, k
        }
    }

    String getServiceName() {
        'sshd'
    }

    String getScriptServiceName() {
        'sshd/debian/8'
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

    HostServices putServices(HostServices services) {
        services.putAvailableService 'sshd', sshdFactory
        services.putAvailableScriptService 'sshd/debian/8', sshdDebianFactory
    }

    List getAdditionalModules() {
        [
            new SshdModule(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new CmdImplModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TokensTemplateModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    install(new FactoryModuleBuilder().implement(HostServiceScript, Sshd_Debian_8).build(Sshd_Debian_8_Factory))
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
