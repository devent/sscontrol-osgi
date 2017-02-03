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
package com.anrisoftware.sscontrol.hosts.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Hosts_Linux_ServerTest extends AbstractTest_Hosts_Linux {

    @Test
    void "test_server"() {
        def test = [
            name: 'test_server',
            input: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "hosts" with {
    ip '127.0.0.1', host: 'localhost', alias: 'robobee-test', on: 'address'
    ip '192.168.56.120', host: 'andrea-master.muellerpublic.de', alias: 'andrea-master'
    ip '127.0.0.1', host: 'robobee-test.muellerpublic.de', alias: 'robobee-test'
}
""",
            expected: { Map args ->
                assertStringResource Hosts_Linux_ServerTest, readRemoteFile('/etc/hosts'), "${args.test.name}_hosts_expected.txt"
            },
        ]
        doTest test
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }
}
