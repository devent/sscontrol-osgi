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
package com.anrisoftware.sscontrol.rkt.debian.internal.rkt_1_2x_debian_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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
class RktServerTest extends AbstractRktRunnerTest {

    @Test
    void "rkt_server"() {
        def test = [
            name: "rkt_server",
            script: """
service "ssh", host: "robobee@robobee-test", socket: "$robobeeSocket"
service "rkt", version: "1.26"
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource RktServerTest, checkRemoteFiles('/usr/bin/rkt*'), "${args.test.name}_bin_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        new File(robobeeSocket).exists()
        assumeTrue testHostAvailable
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
