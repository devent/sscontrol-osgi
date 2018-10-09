package com.anrisoftware.sscontrol.rkt.script.debian.internal.rkt_1_2x.debian_9

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
class RktServerTest extends AbstractRktRunnerTest {

    @Test
    void "server_rkt_basic"() {
        def test = [
            name: "server_rkt_basic",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "rkt", version: "1.28"
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource RktServerTest, checkRemoteFiles('/usr/bin/rkt*'), "${args.test.name}_bin_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
    }

    @Override
    void createDummyCommands(File dir) {
    }

    @Override
    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
