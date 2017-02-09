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
package com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal

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
class Sshd_Debian_8_Andrea_Master_Test extends AbstractTestSshd_Debian_8 {

    @Test
    void "andrea_master_nodes"() {
        def test = [
            name: "andrea_master_nodes",
            input: """
service "ssh", group: "andrea-master", host: "robobee@andrea-master", key: "$robobeeKey"
service "ssh", group: "andrea-nodes", key: "$robobeeKey" with {
    host "robobee@andrea-node-1"
}
service target: "andrea-master", "sshd"
service target: "andrea-nodes", "sshd"
""",
            expected: { Map args ->
                File dir = args.dir as File
            },
        ]
        doTest test
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }

    void createDummyCommands(File dir) {
    }
}