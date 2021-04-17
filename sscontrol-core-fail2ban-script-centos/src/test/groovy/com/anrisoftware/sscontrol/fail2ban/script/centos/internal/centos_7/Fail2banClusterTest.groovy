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
package com.anrisoftware.sscontrol.fail2ban.script.centos.internal.centos_7

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(Nodes3AvailableCondition.class)
class Fail2banClusterTest extends AbstractFail2banRunnerTest {

    @Test
    void "cluster_ssh"() {
        def test = [
            name: "cluster_ssh",
            script: '''
service "ssh", group: "servers" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
service "fail2ban", target: "servers" with {
    debug "debug", level: 4
    banning time: "PT1M"
    jail "sshd"
}
''',
            scriptVars: [sockets: nodesSockets],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/fail2ban.local', 'robobee-test.test'), "${args.test.name}_fail2ban_local_expected.txt"
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/jail.local', 'robobee-test.test'), "${args.test.name}_jail_local_expected.txt"
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/action.d/ufw.conf', 'robobee-test.test'), "${args.test.name}_ufw_conf_expected.txt"
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
