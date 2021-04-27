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
package com.anrisoftware.sscontrol.sshd.script.centos.internal.centos_7

import static com.anrisoftware.sscontrol.shell.external.utils.MailSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.MailSocketCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(MailSocketCondition.class)
class SshdServerTest extends AbstractSshdRunnerTest {

    @Test
    void "server_basic"() {
        def test = [
            name: "server_basic",
            script: '''
service "ssh", host: "robobee@${testHost}", socket: testSocket
service "sshd"
''',
            scriptVars: [testHost: mailHost, testSocket: mailSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource SshdServerTest, readPrivilegedRemoteFile('/etc/ssh/sshd_config', mailHost), "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_allow_user"() {
        def test = [
            name: "server_allow_user",
            script: '''
service "ssh", host: "robobee@${testHost}", socket: testSocket
service "sshd" with {
    allowUser << 'robobee'
}
''',
            scriptVars: [testHost: mailHost, testSocket: mailSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource SshdServerTest, readPrivilegedRemoteFile('/etc/ssh/sshd_config', mailHost), "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_binding_port"() {
        def test = [
            name: "server_binding_port",
            script: '''
service "ssh", host: "robobee@${testHost}", socket: testSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [testHost: mailHost, testSocket: mailSocket],
            expectedServicesSize: 2,
            after: {  Map args ->
                remoteCommand """
sudo bash -s << eof1
cat > /etc/ssh/sshd_config << 'EOL'
${sshdConfig()}
EOL
eof1
sudo semanage port -a -t ssh_port_t -p tcp 22
sudo systemctl restart sshd
""", mailHost, 2222
            },
            expected: { Map args ->
                assertStringResource SshdServerTest, readPrivilegedRemoteFile('/etc/ssh/sshd_config', mailHost, 2222), "${args.test.name}_sshd_config_expected.txt"
            },
        ]
        doTest test
    }

    static final sshdConfig = { IOUtils.toString(SshdServerTest.class.getResource("sshd_config.txt").openStream(), StandardCharsets.UTF_8) }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
