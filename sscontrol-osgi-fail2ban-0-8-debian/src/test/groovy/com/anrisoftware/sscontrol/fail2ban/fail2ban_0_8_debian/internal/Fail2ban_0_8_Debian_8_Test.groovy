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
package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.initfileparser.internal.InitFileParserModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.external.Fail2ban_0_8_Debian_8_Factory
import com.anrisoftware.sscontrol.fail2ban.internal.Fail2banModule
import com.anrisoftware.sscontrol.fail2ban.internal.Fail2banImpl.Fail2banImplFactory
import com.anrisoftware.sscontrol.replace.internal.ReplaceModule
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.Cmd
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImpl
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshModule
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
class Fail2ban_0_8_Debian_8_Test extends AbstractScriptTestBase {

    @Inject
    Fail2banImplFactory fail2banFactory

    @Inject
    Fail2ban_0_8_Debian_8_Factory fail2banDebianFactory

    @Inject
    CmdRunCaller cmdRunCaller

    static Map expectedResources = [
        default_target_sudo: Fail2ban_0_8_Debian_8_Test.class.getResource('default_target_sudo_expected.txt'),
        default_target_apt_get: Fail2ban_0_8_Debian_8_Test.class.getResource('default_target_apt_get_expected.txt'),
        default_target_service: Fail2ban_0_8_Debian_8_Test.class.getResource('default_target_service_expected.txt'),
        default_target_jail_local: Fail2ban_0_8_Debian_8_Test.class.getResource('default_target_jail_local_expected.txt'),
    ]

    @Test
    void "fail2ban script"() {
        def testCases = [
            [
                enabled: true,
                name: "default_target",
                input: """
service "fail2ban"
""",
                expected: { Map args ->
                    File dir = args.dir
                    assertStringContent fileToString(new File(dir, 'sudo.out')), resourceToString(expectedResources["${args.test.name}_sudo"])
                    assertStringContent fileToString(new File(dir, 'apt-get.out')), resourceToString(expectedResources["${args.test.name}_apt_get"])
                    assertStringContent fileToString(new File(dir, 'service.out')), resourceToString(expectedResources["${args.test.name}_service"])
                    assertStringContent fileToString(new File(dir, '/etc/fail2ban/jail.local')), resourceToString(expectedResources["${args.test.name}_jail_local"])
                },
            ],
            [
                enabled: false,
                name: "apache_jail",
                input: """
service "fail2ban" with {
    jail "apache"
}
""",
                expected: { Map args ->
                    File dir = args.dir
                    //                    assertStringContent fileToString(new File(dir, 'sudo.out')), resourceToString(expectedResources["${args.test.name}_sudo"])
                    //                    assertStringContent fileToString(new File(dir, 'apt-get.out')), resourceToString(expectedResources["${args.test.name}_apt_get"])
                    //                    assertStringContent fileToString(new File(dir, 'service.out')), resourceToString(expectedResources["${args.test.name}_service"])
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            if (test.enabled) {
                doTest test, k
            }
        }
    }

    HostServiceScript setupScript(Map args, HostServiceScript script) {
        super.setupScript args, script
        def configDir = new File(args.dir, "/etc/fail2ban")
        script.ufwScript.setJailLocalConfigFileTmp new File(configDir, "jail.local")
        script.ufwScript.setJailLocalConfigFileDest new File(configDir, "jail.local")
        return script
    }

    String getServiceName() {
        'fail2ban'
    }

    String getScriptServiceName() {
        'fail2ban/debian/8'
    }

    void createDummyCommands(File dir) {
        def configDir = new File(dir, 'etc/fail2ban')
        configDir.mkdirs()
        IOUtils.copy(Fail2ban_0_8_Debian_8_Test.class.getResource("fail2ban_conf.txt").openStream(), new FileOutputStream(new File(configDir, "fail2ban.conf")))
        IOUtils.copy(Fail2ban_0_8_Debian_8_Test.class.getResource("jail_local.txt").openStream(), new FileOutputStream(new File(configDir, "jail.local")))
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service',
            'ufw'
        ]
    }

    void putServices(HostServices services) {
        services.putAvailableService 'fail2ban', fail2banFactory
        services.putAvailableScriptService 'fail2ban/debian/8', fail2banDebianFactory
    }

    List getAdditionalModules() {
        [
            new Fail2banModule(),
            new InitFileParserModule(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellModule(),
            new SshModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TokensTemplateModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind Cmd to CmdImpl
                    install(new FactoryModuleBuilder().implement(HostServiceScript, Fail2ban_0_8_Debian_8).build(Fail2ban_0_8_Debian_8_Factory))
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
