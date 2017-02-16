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
package com.anrisoftware.sscontrol.etcd.debian.internal.etcd_3_1

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
class Etcd_3_1_Debian_8_Server_Test extends AbstractTestEtcd_3_1_Debian_8 {

    @Test
    void "etcd_script_basic"() {
        def test = [
            name: "etcd_script_basic",
            input: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "etcd", member: "default"
""",
            expected: { Map args ->
                assertStringResource Etcd_3_1_Debian_8_Server_Test, readRemoteFile('/etc/etcd/etcd.conf'), "${args.test.name}_etcd_conf_expected.txt"
                assertStringResource Etcd_3_1_Debian_8_Server_Test, readRemoteFile('/etc/systemd/system/etcd.service'), "${args.test.name}_etcd_service_expected.txt"
                assertStringResource Etcd_3_1_Debian_8_Server_Test, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue testHostAvailable
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }
}
