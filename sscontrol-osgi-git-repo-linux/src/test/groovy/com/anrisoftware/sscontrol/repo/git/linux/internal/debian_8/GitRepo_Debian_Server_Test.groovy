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
package com.anrisoftware.sscontrol.repo.git.linux.internal.debian_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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
class GitRepo_Debian_Server_Test extends Abstract_Git_Runner_Debian_Test {

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    static final URL wordpressZip = GitRepo_Debian_Server_Test.class.getResource('wordpress-app.zip')

    @Test
    void "git_file"() {
        def test = [
            name: "git_file",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "git", group: 'wordpress-app' with {
    remote url: "/tmp/wordpress-app"
    property << "checkout_directory=\$checkoutDir"
}
""",
            scriptVars: [checkoutDir: '/tmp/w'],
            expectedServicesSize: 2,
            before: { Map test ->
                def file = SSH.escape('wordpress-app.zip')
                execRemoteFile """
if ! which unzip; then
sudo DEBIAN_FRONTEND=noninteractive apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y unzip
fi
mkdir -p /tmp/wordpress-app
cd /tmp/wordpress-app
cat > ${file}
unzip $file
""", wordpressZip.openStream()
            },
            after: { Map test -> remoteCommand """
rm -rf "${test.scriptVars.checkoutDir}"
rm -r /tmp/wordpress-app
""" },
            expected: { Map args ->
                assertStringResource GitRepo_Debian_Server_Test, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_yaml_expected.txt"
                assertStringResource GitRepo_Debian_Server_Test, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "git_ssh"() {
        def test = [
            name: "git_file",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "git", group: 'wordpress-app' with {
    remote url: "git@github.com:devent/wordpress-app-test.git"
    credentials "ssh", key: "$robobeeKey"
    property << "checkout_directory=\$checkoutDir"
}
""",
            scriptVars: [checkoutDir: '/tmp/w'],
            expectedServicesSize: 2,
            after: { Map test -> remoteCommand """
rm -rf "${test.scriptVars.checkoutDir}"
""" },
            expected: { Map args ->
                assertStringResource GitRepo_Debian_Server_Test, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'mysql-deployment.yaml').absolutePath), "${args.test.name}_mysql_deployment_yaml_expected.txt"
                assertStringResource GitRepo_Debian_Server_Test, readRemoteFile(new File(args.test.scriptVars.checkoutDir, 'wordpress-deployment.yaml').absolutePath), "${args.test.name}_wordpress_deployment_yaml_expected.txt"
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
