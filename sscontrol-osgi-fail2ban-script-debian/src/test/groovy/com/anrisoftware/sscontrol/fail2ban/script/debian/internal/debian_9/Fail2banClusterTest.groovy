package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Fail2banClusterTest extends AbstractFail2banRunnerTest {

    @Test
    void "server_ssh"() {
        def test = [
            name: "server_ssh",
            script: '''
service "ssh", group: "servers" with {
    host "robobee@robobee-test", socket: "/tmp/robobee@robobee-test:22"
    host "robobee@robobee-1-test", socket: "/tmp/robobee@robobee-1-test:22"
    host "robobee@robobee-1-test", socket: "/tmp/robobee@robobee-2-test:22"
}
service "fail2ban", target: "servers" with {
    debug "debug", level: 4
    banning time: "PT1M"
    jail "sshd"
}
''',
            scriptVars: [:],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue new File('/tmp/robobee@robobee-test:22').exists()
        assumeTrue new File('/tmp/robobee@robobee-1-test:22').exists()
        assumeTrue new File('/tmp/robobee@robobee-2-test:22').exists()
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
