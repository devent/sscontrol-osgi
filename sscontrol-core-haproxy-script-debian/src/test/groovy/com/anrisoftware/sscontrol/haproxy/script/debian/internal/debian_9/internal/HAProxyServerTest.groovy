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
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(RobobeeSocketCondition.class)
class HAProxyServerTest extends AbstractHAProxyRunnerTest {

    @Test
    void "haproxy_server_proxies"() {
        def test = [
            name: "haproxy_server_proxies",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "haproxy", version: "1.8" with {
    proxy "http" with {
        backend address: "192.168.56.200", port: 30000
    }
    proxy "https" with {
        frontend name: "andrea-node-1", address: "192.168.56.200"
        backend address: "192.168.56.200", port: 30001
    }
    proxy "ssh" with {
        frontend name: "andrea-node-1", address: "192.168.56.200", port: 22
        backend address: "192.168.56.200", port: 30022
    }
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource HAProxyServerTest, readRemoteFile('/etc/haproxy/haproxy.cfg'), "${args.test.name}_haproxy_cfg_expected.txt"
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
