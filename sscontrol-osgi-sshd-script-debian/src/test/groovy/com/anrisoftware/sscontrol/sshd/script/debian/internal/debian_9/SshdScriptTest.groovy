package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

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
class SshdScriptTest extends AbstractSshdScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd"
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource SshdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource SshdScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource SshdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_binding_port"() {
        def test = [
            name: "script_binding_port",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_ufw_binding_port"() {
        def test = [
            name: "script_ufw_binding_port",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "sshd" with {
    bind port: 2222
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            before: { Map test ->
                File dir = test.dir
                createEchoCommand dir, 'which'
                createCommand ufwActiveCommand, test.dir, 'ufw'
            },
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource SshdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource SshdScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
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
