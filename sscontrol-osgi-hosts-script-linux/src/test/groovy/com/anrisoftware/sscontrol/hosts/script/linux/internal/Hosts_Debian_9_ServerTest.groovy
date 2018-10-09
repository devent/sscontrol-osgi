package com.anrisoftware.sscontrol.hosts.script.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

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
class Hosts_Debian_9_ServerTest extends AbstractTestHosts {

    @Test
    void "debian_9_test_server"() {
        def test = [
            name: 'debian_9_test_server',
            input: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "hosts" with {
    ip '192.168.56.200', host: 'robobee-test.test', alias: 'robobee-test, andrea-master.robobee-test, andrea-master', on: "address"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expected: { Map args ->
                assertStringResource Hosts_Debian_9_ServerTest, readRemoteFile('/etc/hosts'), "${args.test.name}_hosts_expected.txt"
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
