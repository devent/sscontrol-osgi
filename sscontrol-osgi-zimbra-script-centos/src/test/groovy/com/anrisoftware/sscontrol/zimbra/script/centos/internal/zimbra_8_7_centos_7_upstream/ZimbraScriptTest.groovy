package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

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
class ZimbraScriptTest extends AbstractZimbraScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "zimbra", version: "8.7"
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ZimbraScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "yum.out", "${args.test.name}_yum_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_lets_encrypt"() {
        def test = [
            name: "script_lets_encrypt",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "zimbra", version: "8.7" with {
    domain email: "erwin.mueller82@gmail.com"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            before: { Map test ->
                File dir = test.dir
                new File(dir, '/sbin').mkdirs()
            },
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ZimbraScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "yum.out", "${args.test.name}_yum_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
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
