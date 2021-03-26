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
package com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.CentosSocketCondition.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.CentosSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(CentosSocketCondition.class)
class NfsServerTest extends AbstractNfsRunnerTest {

    @Test
    void "nfs_server_exports"() {
        def test = [
            name: "nfs_server_exports",
            script: '''
service "ssh", host: "robobee@192.168.56.230", socket: robobeeSocket
service "nfs", version: "1.3" with {
    export dir: "/nfsfileshare/0" with {
        host << "andrea-node-0.muellerpublic.de"
        host name: "andrea-node-1.muellerpublic.de", options: "rw,sync,no_root_squash"
    }
}
''',
            scriptVars: [robobeeSocket: centosSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource NfsServerTest, readRemoteFile('/etc/exports', "mail.robobee.test"), "${args.test.name}_exports_expected.txt"
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
