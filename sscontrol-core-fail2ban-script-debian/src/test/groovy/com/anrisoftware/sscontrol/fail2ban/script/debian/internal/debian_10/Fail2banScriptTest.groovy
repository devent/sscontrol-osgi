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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_10

import static com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@EnabledIfSystemProperty(named = 'project.custom.local.tests.enabled', matches = 'true')
@ExtendWith(LocalhostSocketCondition.class)
class Fail2banScriptTest extends AbstractFail2banScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "fail2ban"
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Fail2banScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "etc/fail2ban/fail2ban.local", "${args.test.name}_fail2ban_local_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "etc/fail2ban/jail.local", "${args.test.name}_jail_local_expected.txt"
                assertFileResource Fail2banScriptTest, gen, "etc/fail2ban/action.d/ufw.conf", "${args.test.name}_ufw_conf_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "etc/fail2ban/filter.d/sshd.conf", "${args.test.name}_sshd_conf_expected.txt"
            }
        ]
        doTest test
    }

    @Test
    void "script_debug"() {
        def test = [
            name: "script_debug",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "fail2ban" with {
    debug "debug", level: 5
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Fail2banScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                //assertFileResource Fail2banScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "etc/fail2ban/fail2ban.local", "${args.test.name}_fail2ban_local_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "etc/fail2ban/jail.local", "${args.test.name}_jail_local_expected.txt"
            }
        ]
        doTest test
    }

    @Test
    void "script_custom_port"() {
        def test = [
            name: "script_custom_port",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "fail2ban" with {
    jail "apache", port: 22222
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Fail2banScriptTest, dir, "etc/fail2ban/jail.local", "${args.test.name}_jail_local_expected.txt"
            }
        ]
        doTest test
    }
}
