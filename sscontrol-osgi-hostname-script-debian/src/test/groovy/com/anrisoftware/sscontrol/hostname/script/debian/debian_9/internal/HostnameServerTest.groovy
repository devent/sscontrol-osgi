package com.anrisoftware.sscontrol.hostname.script.debian.debian_9.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class HostnameServerTest extends AbstractTestHostname {

    @Test
    void "hostname_server_fqdn"() {
        def test = [
            name: "hostname_server_fqdn",
            input: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "hostname" with {
    set fqdn: "robobee-test.test"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expected: { Map args ->
                assertStringResource HostnameServerTest, readRemoteFile('/etc/hostname'), "${args.test.name}_hostname_expected.txt"
                assertStringResource HostnameServerTest, remoteCommand('hostname -f'), "${args.test.name}_hostname_f_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void beforeMethod() {
        checkRobobeeSocket()
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
