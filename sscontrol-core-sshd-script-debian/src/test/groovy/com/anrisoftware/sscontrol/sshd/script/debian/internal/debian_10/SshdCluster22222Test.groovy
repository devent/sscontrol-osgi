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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(Nodes3Port22222AvailableCondition.class)
class SshdCluster22222Test extends AbstractSshdRunnerTest {

    @Test
    void "cluster basic"() {
        def test = [
            name: "cluster_basic",
            script: '''
service "ssh", group: "masters" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
}

service "ssh", group: "nodes" with {
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}

service "sshd", target: "masters" with {
    bind port: 22
}

service "sshd", target: "nodes"  with {
    bind port: nodesSshPort
}
''',
            scriptVars: [sockets: nodesSockets, nodesSshPort: 22222],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource SshdCluster22222Test, readRemoteFile('/etc/ssh/sshd_config', 'node-0.robobee-test.test', 22), "${args.test.name}_sshd_config_node_0_expected.txt"
                assertStringResource SshdCluster22222Test, readRemoteFile('/etc/ssh/sshd_config', 'node-1.robobee-test.test', 22222), "${args.test.name}_sshd_config_node_1_expected.txt"
                assertStringResource SshdCluster22222Test, readRemoteFile('/etc/ssh/sshd_config', 'node-2.robobee-test.test', 22222), "${args.test.name}_sshd_config_node_2_expected.txt"
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
