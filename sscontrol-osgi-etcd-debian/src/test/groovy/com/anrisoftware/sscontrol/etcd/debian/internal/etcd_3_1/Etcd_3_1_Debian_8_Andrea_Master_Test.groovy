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

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Etcd_3_1_Debian_8_Andrea_Master_Test extends AbstractTestEtcd_3_1_Debian_8 {

    @Test
    void "andrea_master"() {
        def test = [
            name: "andrea_master",
            input: """
service "ssh", host: "robobee@andrea-master", key: "$robobeeKey"
service "etcd", member: "etcd-0" with {
    bind scheme: "http", address: "localhost", port: 2379
    bind scheme: "https", address: "185.24.220.41", port: 2379
    advertise scheme: "https", address: "etcd-0.muellerpublic.de", port: 2379
    tls cert: '$certCertPem', key: '$certKeyPem'
    authentication "cert", ca: "$certCaPem"
}
""",
            expected: { Map args ->
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
