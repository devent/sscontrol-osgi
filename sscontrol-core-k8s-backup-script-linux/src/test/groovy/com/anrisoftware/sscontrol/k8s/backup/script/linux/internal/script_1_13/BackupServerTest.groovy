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
package com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_13

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
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
class BackupServerTest extends AbstractBackupRunnerTest {

    @Test
    void "backup_service_server"() {
        def test = [
            name: "backup_service_server",
            script: '''
service "ssh" with {
    host "robobee@robobee-test", socket: robobeeSocket
}
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', cert: certs.cert, key: certs.key
}
service "backup" with {
    service namespace: "wordpress", name: "db"
    destination dir: backupDir
    client key: rsyncKey, proxy: true, timeout: "1h"
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                backupDir: '/tmp/w',
                certs: [ cert: certCertPem, key: certKeyPem],
                rsyncKey: rsyncKey,
            ],
            expectedServicesSize: 3,
            before: { Map test -> setupServer test: test },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource BackupServerTest, readRemoteFile(new File('/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    def setupServer(Map args) {
        tearDownServer args
        def file = 'wordpress-app.zip'
        remoteCommand """
echo "Create /tmp/kubectl."
cat > /tmp/kubectl << 'EOL'
${KUBECTL_COMMAND()}
EOL
chmod +x /tmp/kubectl

echo "Create ${args.test.scriptVars.backupDir}."
rm -rf "${args.test.scriptVars.backupDir}"
mkdir -p "${args.test.scriptVars.backupDir}"
"""
    }

    def tearDownServer(Map args) {
        remoteCommand """
rm /tmp/kubectl
rm /tmp/kubectl.out
rm -rf "${args.test.scriptVars.backupDir}"
"""
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
