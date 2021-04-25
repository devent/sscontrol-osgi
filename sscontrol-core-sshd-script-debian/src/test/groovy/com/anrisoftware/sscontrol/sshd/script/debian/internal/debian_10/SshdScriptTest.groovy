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
package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_10

import static com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_10_TestUtils.*

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
@EnabledIfSystemProperty(named = "project.custom.local.tests.enabled", matches = 'true')
@ExtendWith(LocalhostSocketCondition.class)
class SshdScriptTest extends AbstractSshdScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd"
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource SshdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource SshdScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource SshdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource SshdScriptTest, dir, "etc/ssh/sshd_config", "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_allow_user"() {
        def test = [
            name: "script_allow_user",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd" with {
    allowUser << 'robobee'
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "etc/ssh/sshd_config", "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_binding_port"() {
        def test = [
            name: "script_binding_port",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_ufw_binding_port"() {
        def test = [
            name: "script_ufw_binding_port",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            before: { Map test ->
                File dir = test.dir
                createEchoCommand dir, 'which'
                createCommand ufwActiveCommand, test.dir, 'ufw'
            },
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource SshdScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
            },
        ]
        doTest test
    }
}
