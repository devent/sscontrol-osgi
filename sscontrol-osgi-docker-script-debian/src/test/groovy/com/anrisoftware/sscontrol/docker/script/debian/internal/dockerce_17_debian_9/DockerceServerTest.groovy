package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class DockerceServerTest extends AbstractDockerceRunnerTest {

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    @Test
    void "server_docker"() {
        def test = [
            name: "server_docker",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "docker"
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource DockerceServerTest, readRemoteFile(new File('/etc/apt/sources.list.d', 'docker.list').absolutePath), "${args.test.name}_docker_list_expected.txt"
                assertStringResource DockerceServerTest, remoteCommand("docker --version"), "${args.test.name}_docker_version_expected.txt"
            },
        ]
        doTest test
    }

    @Before
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
