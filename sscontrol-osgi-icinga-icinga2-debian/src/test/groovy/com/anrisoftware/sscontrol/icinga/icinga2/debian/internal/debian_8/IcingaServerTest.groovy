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
package com.anrisoftware.sscontrol.icinga.icinga2.debian.internal.debian_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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
class IcingaServerTest extends AbstractIcingaDebianRunnerTest {

    @Test
    void "basic_server"() {
        def test = [
            name: "basic_server",
            script: """
service "ssh", host: "robobee@robobee-test", socket: "$robobeeSocket"
service "icinga", version: "2"
""",
            scriptVars: [:],
            expectedServicesSize: 2,
            before: { Map test -> },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource IcingaServerTest, readRemoteFile(new File('/etc/apt/sources.list.d', 'icinga.list').absolutePath), "${args.test.name}_icinga_list_expected.txt"
                assertStringResource IcingaServerTest, readRemoteFile(new File('/etc/apt/sources.list.d', 'backports.list').absolutePath), "${args.test.name}_backports_list_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "ido_mysql_server"() {
        def test = [
            name: "ido_mysql_server",
            script: """
service "ssh", host: "robobee@robobee-test", socket: "$robobeeSocket"
service "icinga", version: "2" with {
    plugin << "ido-mysql"
}
""",
            scriptVars: [:],
            expectedServicesSize: 2,
            before: { Map test -> },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource IcingaServerTest, readRemoteFile(new File('/etc/apt/sources.list.d', 'icinga.list').absolutePath), "${args.test.name}_icinga_list_expected.txt"
                assertStringResource IcingaServerTest, readRemoteFile(new File('/etc/apt/sources.list.d', 'backports.list').absolutePath), "${args.test.name}_backports_list_expected.txt"
            },
        ]
        doTest test
    }

    def tearDownServer(Map args) {
        remoteCommand """
"""
    }

    @Before
    void beforeMethod() {
        assumeTrue new File(robobeeSocket).exists()
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
