package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux

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
class DockerRegistryScriptTest extends AbstractDockerRegistryScriptTest {

    @Test
    void "script_docker_tls_runner"() {
        def test = [
            name: "script_docker_tls_runner",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "repo-git", group: 'wordpress-app' with {
    remote url: "/user/wordpress-app.git"
}
service "registry-docker", group: "default" with {
    host address: "docker:2376", ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    registry port: 5000, ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    credentials "user", name: "devent", password: "xx"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 3,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource DockerRegistryScriptTest, dir, "dpkg.out", "${args.test.name}_dpkg_expected.txt"
                assertFileResource DockerRegistryScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource DockerRegistryScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource DockerRegistryScriptTest, dir, "cat.out", "${args.test.name}_cat_expected.txt"
                assertFileResource DockerRegistryScriptTest, dir, "git.out", "${args.test.name}_git_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
