/*-
 * #%L
 * sscontrol-osgi - hosts-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.hosts.script.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Hosts_Debian_9_ServerTest extends AbstractTestHosts {

    @Test
    void "debian_9_test_server"() {
        def test = [
            name: 'debian_9_test_server',
            input: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "hosts" with {
    ip '192.168.56.200', host: 'robobee-test.test', alias: 'robobee-test, andrea-master.robobee-test, andrea-master', on: "address"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expected: { Map args ->
                assertStringResource Hosts_Debian_9_ServerTest, readRemoteFile('/etc/hosts'), "${args.test.name}_hosts_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void beforeMethod() {
        checkRobobeeSocket()
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
