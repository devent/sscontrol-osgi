/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class SshdServerTest extends AbstractSshdRunnerTest {

    @Test
    void "server_basic"() {
        def test = [
            name: "server_basic",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "sshd"
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource SshdServerTest, readRemoteFile('/etc/ssh/sshd_config'), "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_binding_port"() {
        def test = [
            name: "server_binding_port",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            after: {  Map args ->
                remoteCommand """
sudo bash -s << eof1
cat > /etc/ssh/sshd_config << 'EOL'
${sshdConfig()}
EOL
eof1
sudo systemctl restart sshd
""", 'robobee-test', 2222
            },
            expected: { Map args ->
                assertStringResource SshdServerTest, readRemoteFile('/etc/ssh/sshd_config', 'robobee-test', 2222), "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_ufw_binding_port"() {
        def test = [
            name: "server_ufw_binding_port",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            after: {  Map args ->
                remoteCommand """
sudo bash -s << eof1
cat > /etc/ssh/sshd_config << 'EOL'
${sshdConfig()}
EOL
eof1
sudo systemctl restart sshd
""", 'robobee-test', 2222
            },
            expected: { Map args ->
                assertStringResource SshdServerTest, readRemoteFile('/etc/ssh/sshd_config', 'robobee-test', 2222), "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    static final sshdConfig = { IOUtils.toString(SshdServerTest.class.getResource("sshd_config.txt").openStream(), StandardCharsets.UTF_8) }

    @Before
    void beforeMethod() {
        assumeSocketExists robobeeSocket
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
