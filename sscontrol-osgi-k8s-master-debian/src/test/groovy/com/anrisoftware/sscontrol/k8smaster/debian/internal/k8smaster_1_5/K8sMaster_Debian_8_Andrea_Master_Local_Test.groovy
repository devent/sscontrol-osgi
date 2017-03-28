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
package com.anrisoftware.sscontrol.k8smaster.debian.internal.k8smaster_1_5

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
class K8sMaster_Debian_8_Andrea_Master_Local_Test extends AbstractTest_K8sMaster_Debian_8 {

    @Test
    void "andrea_master_local"() {
        def test = [
            name: "andrea_master_local",
            input: """
service "ssh", group: "master", key: "${robobeeKey}" with {
    host "robobee@andrea-master-local"
}
service "ssh", group: "nodes", key: "${robobeeKey}" with {
    host "robobee@andrea-node-0-local"
}
service "ssh", group: "etcd", key: "${robobeeKey}" with {
    host "robobee@etcd-0.robobee.test"
    host "robobee@etcd-1.robobee.test"
}
service "k8s-master", name: "andrea-cluster", target: "master", advertise: targets['master'][0] with {
    tls certs.k8s
    authentication "cert", ca: certs.k8s.ca
    plugin "flannel"
    plugin "calico"
    plugin "etcd", address: "https://192.168.56.200:2379,https://192.168.56.220:2379" with {
        tls certs.etcd
    }
}
""",
            scriptVars: ["certs": andreaLocalCerts],
            generatedDir: folder.newFolder(),
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

    static final Map andreaLocalCerts = [
        k8s: [
            ca: AbstractTest_K8sMaster_Debian_8.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: AbstractTest_K8sMaster_Debian_8.class.getResource('andrea_local_k8smaster_robobee_test_cert.pem'),
            key: AbstractTest_K8sMaster_Debian_8.class.getResource('andrea_local_k8smaster_robobee_test_key_insecure.pem'),
        ],
        etcd: [
            ca: AbstractTest_K8sMaster_Debian_8.class.getResource('andrea_local_etcd_ca_cert.pem'),
            cert: AbstractTest_K8sMaster_Debian_8.class.getResource('andrea_local_etcd_client_0_robobee_test_cert.pem'),
            key: AbstractTest_K8sMaster_Debian_8.class.getResource('andrea_local_etcd_client_0_robobee_test_key_insecure.pem'),
        ]
    ]

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }
}
