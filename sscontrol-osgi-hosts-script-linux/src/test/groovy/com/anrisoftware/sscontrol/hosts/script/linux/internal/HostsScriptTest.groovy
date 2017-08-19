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
package com.anrisoftware.sscontrol.hosts.script.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class HostsScriptTest extends AbstractTestHosts {

    @Test
    void "explicit_list"() {
        def test = [
            name: "explicit_list",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "hosts" with {
    ip "192.168.0.52", host: "srv1.ubuntutest.com"
    ip "192.168.0.49", host: "srv1.ubuntutest.de", alias: "srv1"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource HostsScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource HostsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HostsScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource HostsScriptTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
                assertFileResource HostsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HostsScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
