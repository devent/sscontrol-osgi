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
package com.anrisoftware.sscontrol.docker.debian.internal.docker_1_12

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
class Docker_Debian_8_Andrea_Master_Local_Test extends AbstractTest_Docker_Debian_8 {

    @Test
    void "andrea_master_local"() {
        def test = [
            name: "andrea_master_local",
            input: """
service "ssh", group: "master", host: "robobee@andrea-master-local", key: "$robobeeKey"
service "ssh", group: "nodes", key: "$robobeeKey" with {
    host "robobee@andrea-node-1-local"
}
service "docker", target: "master"
service "docker", target: "nodes"
""",
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue(isHostAvailable([
            'andrea-master-local',
            'andrea-node-1-local'
        ]))
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }
}
