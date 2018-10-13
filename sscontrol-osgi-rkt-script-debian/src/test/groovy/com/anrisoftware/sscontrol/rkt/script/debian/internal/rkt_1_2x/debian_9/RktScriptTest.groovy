package com.anrisoftware.sscontrol.rkt.script.debian.internal.rkt_1_2x.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

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
class RktScriptTest extends AbstractRktScriptTest {

    @Test
    void "script_rkt_basic"() {
        def test = [
            name: "script_rkt_basic",
            expectedServicesSize: 2,
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "rkt", version: "1.29"
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource RktScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource RktScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource RktScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
