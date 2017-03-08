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
class Etcd_3_1_Debian_8_Andrea_Master_Local_Test extends AbstractTestEtcd_3_1_Debian_8 {

    @Test
    void "andrea_master_local"() {
        def test = [
            name: "andrea_master_local",
            input: """
service "ssh", group: "andrea-master", host: "robobee@andrea-master-local", key: "$robobeeKey"
service "ssh", group: "andrea-nodes", key: "$robobeeKey" with {
    host "robobee@andrea-node-0-local"
}

targets['all'].eachWithIndex { host, i ->
    service "etcd", target: host, member: "etcd-\${i}" with {
        debug "debug", level: 1
        bind "https://\${host.hostAddress}:2379"
        advertise "https://etcd-\${i}.robobee.test:2379"
        tls cert: "\${certs["etcd_\${i}_cert"]}", key: "\${certs["etcd_\${i}_key"]}"
        authentication "cert", ca: "\${certs["ca"]}"
        peer state: "new", advertise: "https://etcd-\${i}.robobee.test:2380", listen: "https://\${host.hostAddress}:2380", token: "andrea-etcd-cluster-1" with {
            cluster << "etcd-0=https://etcd-0.robobee.test:2380"
            cluster << "etcd-1=https://etcd-1.robobee.test:2380"
            tls cert: "\${certs["etcd_\${i}_cert"]}", key: "\${certs["etcd_\${i}_key"]}"
            authentication "cert", ca: "\${certs["ca"]}"
        }
    }
}
""",
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue isHostAvailable([
            'andrea-master-local',
            'andrea-node-0-local'
        ])
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }
}
