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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(RobobeeSocketCondition.class)
class Fail2banServerTest extends AbstractFail2banRunnerTest {

    @Test
    void "server_ssh"() {
        def test = [
            name: "server_ssh",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "fail2ban" with {
    debug "debug", level: 5
    banning time: "PT1M"
    jail "sshd"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/fail2ban.local'), "${args.test.name}_fail2ban_local_expected.txt"
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/jail.local'), "${args.test.name}_jail_local_expected.txt"
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/action.d/ufw.conf'), "${args.test.name}_ufw_conf_expected.txt"
            },
        ]
        doTest test
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
