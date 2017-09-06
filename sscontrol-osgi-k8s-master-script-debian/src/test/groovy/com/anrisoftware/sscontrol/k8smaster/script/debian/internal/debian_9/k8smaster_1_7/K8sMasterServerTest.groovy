/*
 l * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7

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
class K8sMasterServerTest extends AbstractMasterRunnerTest {

    @Test
    void "server_tls"() {
        def test = [
            name: "server_tls",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-master", name: "andrea-test-cluster", advertise: "192.168.56.200" with {
    bind secure: "192.168.56.200"
    tls certs.tls
    authentication "cert", ca: certs.tls.ca
    plugin "etcd", address: "https://192.168.56.200:2379" with {
        tls certs.etcd
    }
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls certs.tls
    }
}
''',
            scriptVars: [robobeeSocket: robobeeSocket, certs: robobeetestCerts],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
