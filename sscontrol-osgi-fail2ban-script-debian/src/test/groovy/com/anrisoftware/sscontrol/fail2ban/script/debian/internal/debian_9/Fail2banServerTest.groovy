package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

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
class Fail2banServerTest extends AbstractFail2banRunnerTest {

    @Test
    void "server_ssh"() {
        def test = [
            name: "server_ssh",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "fail2ban" with {
    debug "debug", level: 4
    banning time: "PT1M"
    jail "sshd"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/fail2ban.local'), "${args.test.name}_fail2ban_local_expected.txt"
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/jail.local'), "${args.test.name}_jail_local_expected.txt"
                assertStringResource Fail2banServerTest, readRemoteFile('/etc/fail2ban/action.d/ufw.conf'), "${args.test.name}_ufw_conf_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void beforeMethod() {
        assumeSocketExists robobeeSocket
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
