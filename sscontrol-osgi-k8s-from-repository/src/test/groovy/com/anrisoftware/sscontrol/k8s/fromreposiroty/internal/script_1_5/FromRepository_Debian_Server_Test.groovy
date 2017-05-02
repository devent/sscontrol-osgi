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

import org.apache.commons.codec.Charsets
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil
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
class FromRepository_Debian_Server_Test extends Abstract_FromRepository_Runner_Debian_Test {

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    static final URL wordpressZip = FromRepository_Debian_Server_Test.class.getResource('wordpress-app.zip')

    @Test
    void "git_file_server"() {
        def test = [
            name: "git_file_server",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', cert: '$certCertPem', key: '$certKeyPem'
}
service "git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=\$checkoutDir"
}
service "from-repository", repo: "wordpress-app" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
""",
            scriptVars: [checkoutDir: '/tmp/w'],
            expectedServicesSize: 4,
            before: { Map test ->
                def file = SSH.escape('wordpress-app.zip')
                execRemoteFile """
echo "Create /tmp/kubectl."
cat > /tmp/kubectl << 'EOL'
${IOUtils.toString(echoCommand.openStream(), Charsets.UTF_8)}
EOL
chmod +x /tmp/kubectl

echo "Create ${test.scriptVars.checkoutDir}."
rm -rf "${test.scriptVars.checkoutDir}"
mkdir -p "${test.scriptVars.checkoutDir}"

echo "Install unzip."
if ! which unzip; then
sudo apt-get update
sudo apt-get install unzip
fi

echo "Unzip to /tmp/wordpress-app."
mkdir -p /tmp/wordpress-app
cd /tmp/wordpress-app
cat > ${file}
unzip $file
""", wordpressZip.openStream()
            },
            after: { Map test -> remoteCommand """
rm /tmp/kubectl
rm /tmp/kubectl.out
rm -rf "${test.scriptVars.checkoutDir}"
rm -r /tmp/wordpress-app
""" },
            expected: { Map args ->
                assertStringResource FromRepository_Debian_Server_Test, readRemoteFile(new File('/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
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
