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
package com.anrisoftware.sscontrol.rkt.debian.internal.rkt_1_25_debian_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.external.HostServices

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class K8sMaster_Debian_8_Andrea_Master_Local_Test extends AbstractTest_Rkt_Debian {

    @Test
    void "andrea_master_local"() {
        def test = [
            name: "andrea_master_local",
            input: """
service "ssh", group: "andrea-master", host: "robobee@andrea-master-local", key: "$robobeeKey"
def andreaMaster = targets['andrea-master'][0]
service "k8s-master", name: "andrea-cluster", target: andreaMaster with {
    bind secure: "\${andreaMaster.hostAddress}"
    tls certs.k8s
    authentication "cert", ca: certs.k8s.ca
    plugin "etcd", address: "https://etcd-0.robobee.test" with {
        tls certs.etcd
    }
    kubelet.with {
        tls certs.k8s
    }
}
""",
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
            ca: AbstractTest_Rkt_Debian.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: AbstractTest_Rkt_Debian.class.getResource('andrea_local_k8smaster_robobee_test_cert.pem'),
            key: AbstractTest_Rkt_Debian.class.getResource('andrea_local_k8smaster_robobee_test_key_insecure.pem'),
        ],
        etcd: [
            ca: AbstractTest_Rkt_Debian.class.getResource('andrea_local_etcd_ca_cert.pem'),
            cert: AbstractTest_Rkt_Debian.class.getResource('andrea_local_etcd_etcd_0_robobee_test_cert.pem'),
            key: AbstractTest_Rkt_Debian.class.getResource('andrea_local_etcd_etcd_0_robobee_test_key_insecure.pem'),
        ]
    ]

    Binding createBinding(HostServices services) {
        Binding binding = super.createBinding(services)
        binding.setProperty("certs", andreaLocalCerts)
        return binding
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }
}
