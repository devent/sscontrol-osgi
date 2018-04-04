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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

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
class FromRepositoryManifestsServerTest extends AbstractFromRepositoryRunnerTest {

    static final URL wordpressZip = FromRepositoryManifestsServerTest.class.getResource('wordpress-app_zip.txt')

    static final URL wordpressStZip = FromRepositoryManifestsServerTest.class.getResource('wordpress-app-st_zip.txt')

    static final URL wordpressStgZip = FromRepositoryManifestsServerTest.class.getResource('wordpress-app-stg_zip.txt')

    @Test
    void "apply yaml files from directory"() {
        def test = [
            name: "server_yaml_dir",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test, zipArchive: wordpressZip },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "apply yaml files from ssh"() {
        def test = [
            name: "server_yaml_ssh",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:robobee-repos/wordpress-app-test.git"
    credentials "ssh", key: robobeeKey
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "apply st yaml templates from directory"() {
        def test = [
            name: "server_st_yaml_dir",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test, zipArchive: wordpressStZip },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "apply st yaml templates from ssh with tag"() {
        def test = [
            name: "server_st_yaml_ssh_tag",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:robobee-repos/wordpress-app-test.git"
    checkout tag: "st"
    credentials "ssh", key: robobeeKey
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "apply stg yaml templates from directory"() {
        def test = [
            name: "server_stg_yaml_dir",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test, zipArchive: wordpressStgZip },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "store stg yaml templates from directory"() {
        def test = [
            name: "server_store_stg_yaml_dir",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app", dest: "/tmp/addon/wordpress" with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test, zipArchive: wordpressStgZip },
            after: { Map test ->
                tearDownServer test: test
            },
            expected: { Map args ->
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/addon', 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_expected.txt"
                assertStringResource FromRepositoryManifestsServerTest, readRemoteFile(new File('/tmp/addon', 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "dry run templates"() {
        def test = [
            name: "server_dry_run",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=${checkoutDir}"
}
service "from-repository", repo: "wordpress-app", dryrun: true with {
    property << "kubectl_cmd=/tmp/kubectl"
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                checkoutDir: '/tmp/w'
            ],
            expectedServicesSize: 4,
            before: { Map test -> setupServer test: test, zipArchive: wordpressStgZip },
            after: { Map test ->
                tearDownServer test: test
            },
            expected: { Map args ->
            },
        ]
        doTest test
    }

    def setupServer(Map args) {
        def createArchiveFile = ""
        def archiveStream = null
        if (args.zipArchive) {
            archiveStream = args.zipArchive.openStream()
            createArchiveFile = """
echo "Install unzip."
if ! which unzip; then
sudo apt-get update
sudo apt-get install unzip
fi

echo "Unzip to /tmp/wordpress-app."
mkdir -p /tmp/wordpress-app
cd /tmp/wordpress-app
file="wordpress-app.zip"
cat > \$file
unzip \$file
"""
        }
        execRemoteFile """
mkdir -p /tmp/tmp

file="/tmp/tmp/kubectl"
echo "Create \$file"
cat > \$file << 'EOL'
${IOUtils.toString(echoCommand.openStream(), StandardCharsets.UTF_8)}
EOL
chmod +x \$file

echo "Create ${args.test.scriptVars.checkoutDir}."
rm -rf "${args.test.scriptVars.checkoutDir}"
mkdir -p "${args.test.scriptVars.checkoutDir}"

${createArchiveFile}
""", archiveStream
    }

    def tearDownServer(Map args) {
        remoteCommand """
rm /tmp/tmp/kubectl
rm /tmp/tmp/kubectl.out
rm -rf "${args.test.scriptVars.checkoutDir}"
rm -r /tmp/wordpress-app
"""
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
    }

    Map getScriptEnv(Map args) {
        def env = getEmptyScriptEnv args
        env.base = "/tmp" as File
        env
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
