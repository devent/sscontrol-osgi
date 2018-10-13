package com.anrisoftware.sscontrol.hosts.script.linux.internal

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
class HostsScriptTest extends AbstractTestHosts {

    @Test
    void "explicit_list"() {
        def test = [
            name: "explicit_list",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "hosts" with {
    ip "192.168.0.52", host: "srv1.ubuntutest.com"
    ip "192.168.0.49", host: "srv1.ubuntutest.de", alias: "srv1"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource HostsScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource HostsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HostsScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource HostsScriptTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
                assertFileResource HostsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HostsScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
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
