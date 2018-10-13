package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
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
}
}
''',
            scriptVars: [sockets: sockets, certs: robobeetestEtcdCerts],
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
}
}
''',
            scriptVars: [sockets: sockets, certs: robobeetestEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    static final Map sockets = [
        masters: [
            "/tmp/robobee@robobee-test:22"
        ],
        nodes: [
            "/tmp/robobee@robobee-test:22",
            "/tmp/robobee@robobee-1-test:22",
            "/tmp/robobee@robobee-2-test:22",
        ]
    ]

    @BeforeEach
    void beforeMethod() {
        assumeSocketsExists sockets.nodes
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
