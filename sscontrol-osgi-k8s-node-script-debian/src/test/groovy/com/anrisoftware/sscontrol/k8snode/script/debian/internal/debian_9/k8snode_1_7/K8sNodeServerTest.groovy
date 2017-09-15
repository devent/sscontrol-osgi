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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.debian_9.k8snode_1_7

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*
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
class K8sNodeServerTest extends AbstractNodeRunnerTest {

    @Test
    void "nodes_tls"() {
        def test = [
            name: "nodes_tls",
            script: '''
service "ssh", group: "masters" with {
    host "andrea-master.robobee-test.test", socket: "/tmp/robobee@robobee-test:22"
}
service "ssh", group: "nodes" with {
    host "node-1.robobee-test", socket: "/tmp/robobee@robobee-1-test:22"
}
service "k8s-cluster", target: 'masters' with {
    credentials type: 'cert', name: 'robobee-admin', ca: certs.admin.ca, cert: certs.admin.cert, key: certs.admin.key
}
targets['nodes'].eachWithIndex { host, i ->
service "k8s-node", name: "andrea-node-1-test", target: "nodes", advertise: host.hostAddress, api: targets['masters'] with {
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls certs.tls
        client certs.tls
    }
}
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: robobeetestCerts],
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
            },
        ]
        doTest test
    }

    static final Map robobeetestCerts = [
        tls: [
            ca: K8sNodeServerTest.class.getResource('robobee_test_kube_ca.pem'),
            cert: K8sNodeServerTest.class.getResource('robobee_test_kube_node_1_server_cert.pem'),
            key: K8sNodeServerTest.class.getResource('robobee_test_kube_node_1_server_key.pem'),
        ],
        admin: [
            ca: K8sNodeServerTest.class.getResource('robobee_test_kube_ca.pem'),
            cert: K8sNodeServerTest.class.getResource('robobee_test_kube_admin_cert.pem'),
            key: K8sNodeServerTest.class.getResource('robobee_test_kube_admin_key.pem'),
        ],
    ]

    @Before
    void beforeMethod() {
        assumeTrue new File('/tmp/robobee@robobee-test:22').exists()
        assumeTrue new File('/tmp/robobee@robobee-1-test:22').exists()
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
