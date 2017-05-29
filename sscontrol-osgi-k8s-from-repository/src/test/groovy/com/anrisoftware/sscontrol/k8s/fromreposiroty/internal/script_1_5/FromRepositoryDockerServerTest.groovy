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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.jcabi.ssh.SSH

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FromRepositoryDockerServerTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "docker_build_no_parse_basic_server"() {
        def test = [
            name: "docker_build_no_parse_basic_server",
            script: '''
service "ssh" with {
    host "robobee@robobee-test", socket: robobeeSocket
}
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', cert: cluster_vars.certs.cert, key: cluster_vars.certs.key
}
service "repo-git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=$checkoutDir"
}
service "registry-docker", group: "erwin82" with {
    credentials "user", name: "erwin82", password: "blaue sonne"
}
service "from-repository", repo: "wordpress-app", registry: "erwin82" with {
    property << "kubectl_cmd=/tmp/kubectl"
    property << "docker_cmd=/tmp/docker"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                checkoutDir: '/tmp/w',
                cluster_vars: [ certs: [ cert: certCertPem, key: certKeyPem], ],
            ],
            expectedServicesSize: 5,
            before: { Map test -> setupServer test: test, zipArchive: AbstractFromRepositoryRunnerTest.wordpressZip },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource FromRepositoryDockerServerTest, readRemoteFile(new File('/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    def setupServer(Map args) {
        def file = SSH.escape('wordpress-app.zip')
        execRemoteFile """
echo "Create /tmp/kubectl."
cat > /tmp/kubectl << 'EOL'
${IOUtils.toString(echoCommand.openStream(), StandardCharsets.UTF_8)}
EOL
chmod +x /tmp/kubectl

echo "Create /tmp/docker."
cat > /tmp/docker << 'EOL'
${IOUtils.toString(echoCommand.openStream(), StandardCharsets.UTF_8)}
EOL
chmod +x /tmp/docker

echo "Create ${args.test.scriptVars.checkoutDir}."
rm -rf "${args.test.scriptVars.checkoutDir}"
mkdir -p "${args.test.scriptVars.checkoutDir}"

echo "Install unzip."
if ! which unzip; then
sudo apt-get update
sudo apt-get install unzip
fi

echo "Unzip to /tmp/wordpress-app."
rm -rf /tmp/wordpress-app
mkdir -p /tmp/wordpress-app
cd /tmp/wordpress-app
cat > ${file}
unzip $file
""", args.zipArchive.openStream()
    }

    def tearDownServer(Map args) {
        remoteCommand """
rm /tmp/kubectl
rm /tmp/kubectl.out
rm -rf "${args.test.scriptVars.checkoutDir}"
rm -r /tmp/wordpress-app
"""
    }

    @Before
    void beforeMethod() {
        assumeTrue testHostAvailable
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
