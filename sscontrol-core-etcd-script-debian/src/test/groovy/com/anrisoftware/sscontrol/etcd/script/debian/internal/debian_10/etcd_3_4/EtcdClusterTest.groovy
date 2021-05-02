/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_10.etcd_3_4

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@ExtendWith(Nodes3AvailableCondition.class)
class EtcdClusterTest extends AbstractEtcdRunnerTest {

    @Test
    void "cluster_tls_etcd"() {
        def test = [
            name: "cluster_tls",
            script: '''
service "ssh", group: "etcd" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
targets.etcd.eachWithIndex { host, i ->
    service "etcd", target: host, member: "etcd-${i}", check: targets.etcd[-1] with {
        debug "debug", level: 1
        bind "https://${host.hostAddress}:2379"
        advertise "https://etcd-${i}.robobee-test.test:2379"
        client certs.client
        tls cert: certs.etcd[i].cert, key: certs.etcd[i].key
        authentication "cert", ca: certs.ca
        peer state: "new", advertise: "https://etcd-${i}.robobee-test.test:2380", listen: "https://${host.hostAddress}:2380", token: "robobee-test-cluster-1" with {
            cluster << "etcd-0=https://etcd-0.robobee-test.test:2380"
            cluster << "etcd-1=https://etcd-1.robobee-test.test:2380"
            cluster << "etcd-2=https://etcd-2.robobee-test.test:2380"
            tls cert: certs.etcd[i].cert, key: certs.etcd[i].key
            authentication "cert", ca: certs.ca
        }
        property << "archive_ignore_key=true"
    }
}
''',
            scriptVars: [sockets: nodesSockets, certs: robobeetestEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Test
    void "cluster_tls_gateway"() {
        def test = [
            name: "server_tls_gateway",
            script: '''
service "ssh", group: "etcd" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
targets.etcd.eachWithIndex { host, i ->
    service "etcd", target: host with {
        bind network: "enp0s8:1", "https://10.10.10.7:22379"
        gateway endpoints: "https://etcd-${i}.robobee-test.test:2379"
        client certs.client
        property << "archive_ignore_key=true"
    }
}
''',
            scriptVars: [sockets: nodesSockets, certs: robobeetestEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
