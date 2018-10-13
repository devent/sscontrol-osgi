package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9

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
class DockerceClusterTest extends AbstractDockerceRunnerTest {

    @Test
    void "cluster_docker"() {
        def test = [
            name: "cluster_docker",
            script: '''
service "ssh" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
service "docker"
''',
            scriptVars: [sockets: sockets, certs: certs],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Test
    void "cluster_docker_native_cgroupdriver"() {
        def test = [
            name: "cluster_docker_native_cgroupdriver",
            script: '''
service "ssh" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
service "docker" with {
    property << "native_cgroupdriver=systemd"
}
''',
            scriptVars: [sockets: sockets, certs: certs],
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

    static final Map certs = [
        ca: DockerceClusterTest.class.getResource('registry_robobee_test_test_ca.pem'),
    ]

    @BeforeEach
    void beforeMethod() {
        checkRobobeeSocket()
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
