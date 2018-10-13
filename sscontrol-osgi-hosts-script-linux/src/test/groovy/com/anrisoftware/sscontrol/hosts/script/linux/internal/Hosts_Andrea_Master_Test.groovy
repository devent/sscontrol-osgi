package com.anrisoftware.sscontrol.hosts.script.linux.internal

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
class Hosts_Andrea_Master_Test extends AbstractTestHosts {

    @Test
    void "andrea_master_local_nodes"() {
        def test = [
            name: 'andrea_master_local_nodes',
            input: """
service "ssh", group: "andrea-master", host: "robobee@andrea-master-local", key: "$robobeeKey"
service "ssh", group: "andrea-nodes", key: "$robobeeKey" with {
    host "robobee@andrea-node-0-local"
}
service "hosts", target: "andrea-master" with {
    ip '192.168.56.200', host: 'andrea-master-local.robobee.test', alias: 'andrea-master-local, etcd-0'
    ip '192.168.56.220', host: 'andrea-node-0-local.robobee.test', alias: 'andrea-node-0-local, etcd-1'
}

targets['andrea-nodes'].eachWithIndex { host, i ->
    service "hosts", target: "andrea-nodes" with {
        ip '192.168.56.200', host: 'andrea-master-local.robobee.test', alias: 'andrea-master-local, etcd-0'
        ip '192.168.56.220', host: 'andrea-node-0-local.robobee.test', alias: 'andrea-node-0-local, etcd-1'
    }
}
""",
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @BeforeEach
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
