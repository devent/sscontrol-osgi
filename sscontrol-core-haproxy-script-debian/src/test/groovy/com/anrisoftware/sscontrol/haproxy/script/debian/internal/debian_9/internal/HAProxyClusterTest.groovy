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
package com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(Nodes3Port22222AvailableCondition.class)
class HAProxyClusterTest extends AbstractHAProxyRunnerTest {

    @Test
    void "haproxy_server_proxies"() {
        def test = [
            name: "haproxy_server_proxies",
            script: '''
def edgeNodeTargetHttpPort = 30000
def edgeNodeTargetHttpsPort = 30001
def edgeNodeTargetSshPort = 30022

service "ssh", group: "edge-nodes" with {
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
}

def edgeTargetAddress = targets["edge-nodes"].head().hostAddress

service "haproxy", version: "1.8", target: "edge-nodes" with {
    proxy "http" with {
        frontend address: edgeTargetAddress
        backend address: edgeTargetAddress, port: 30000
    }
    proxy "https" with {
        frontend address: edgeTargetAddress
        backend address: edgeTargetAddress, port: 30001
    }
    proxy "ssh" with {
        frontend address: edgeTargetAddress
        backend address: edgeTargetAddress, port: 30022
    }
}
''',
            scriptVars: [sockets: nodesSockets, robobeeKey: robobeeKey],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource HAProxyClusterTest, readRemoteFile('/etc/haproxy/haproxy.cfg', 'node-1.robobee-test.test', 22222), "${args.test.name}_haproxy_cfg_expected.txt"
            },
        ]
        doTest test
    }

    @Override
    void createDummyCommands(File dir) {
    }

    @Override
    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
