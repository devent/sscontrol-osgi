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
package com.anrisoftware.sscontrol.repo.git.linux.internal

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
class GitRepo_Linux_Server_Test extends Abstract_Git_Runner_Linux_Test {

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    static final URL wordpressZip = GitRepo_Linux_Server_Test.class.getResource('wordpress-app.zip')

    @Test
    void "git_file"() {
        def test = [
            name: "git_file",
            script: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "git", group: 'wordpress-app' with {
    remote url: "/tmp/wordpress-app"
}
""",
            expectedServicesSize: 2,
            before: {
                def file = SSH.escape('wordpress-app.zip')
                execRemoteFile """
if ! which unzip; then
sudo apt-get update
sudo apt-get install unzip
fi
mkdir -p /tmp/wordpress-app
cd /tmp/wordpress-app
cat > ${file}
unzip $file
""", wordpressZip.openStream()
            },
            after: { remoteCommand """
rm -r /tmp/wordpress-app
""" },
            expected: { Map args ->
                assertStringResource GitRepo_Linux_Server_Test, readRemoteFile('/etc/hostname'), "${args.test.name}_hostname_expected.txt"
                assertStringResource GitRepo_Linux_Server_Test, remoteCommand('hostname -f'), "${args.test.name}_hostname_f_expected.txt"
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
