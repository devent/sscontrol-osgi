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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9

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
class K8sNodeClusterTest extends AbstractNodeRunnerTest {

    @Test
    void "nodes_tls"() {
        def test = [
            name: "nodes_tls",
            script: '''
service "ssh", host: "robobee@node-0.robobee-test.test", socket: sockets.masters[0]
service "ssh", group: "masters" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.masters[0]
}
service "ssh", group: "nodes" with {
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[1]
}
service "k8s-cluster", target: 'masters' with {
    context name: 'kubernetes-admin@kubernetes'
}
targets['nodes'].eachWithIndex { host, i ->
    service "k8s-node", target: host, name: "node-${i+1}", join: joinCommand with {
        nodes.labels[i].each { label << it }
        nodes.taints[i].each { taint << it }
    }
}
''',
            scriptVars: [sockets: sockets, certs: robobeetestCerts, nodes: nodes, joinCommand: joinCommand],
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
        etcd: [
            ca: K8sNodeClusterTest.class.getResource('robobee_test_etcd_ca.pem'),
            cert: K8sNodeClusterTest.class.getResource('robobee_test_etcd_kube_0_cert.pem'),
            key: K8sNodeClusterTest.class.getResource('robobee_test_etcd_kube_0_key.pem'),
        ]
    ]

    static final String joinCommand = 'kubeadm join --token 11f7ba.a1f1b40bc527296a 192.168.56.200:443 --discovery-token-ca-cert-hash sha256:7501bc596d3dce2f88ece232d3454876293bea94884bb19f90f2ebc6824e845f'

    static final Map sockets = [
        masters: [
            "/tmp/robobee@robobee-test:22"
        ],
        nodes: [
            "/tmp/robobee@robobee-1-test:22",
            "/tmp/robobee@robobee-2-test:22",
        ]
    ]

    static final Map nodes = [
        labels: [
            [
                "robobeerun.com/role=edge-router",
                "muellerpublic.de/role=web"
            ],
            [
                "muellerpublic.de/role=dev"]
        ],
        taints: [
            [],
            []]
    ]

    @Before
    void beforeMethod() {
        assumeSocketsExists sockets.nodes
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
