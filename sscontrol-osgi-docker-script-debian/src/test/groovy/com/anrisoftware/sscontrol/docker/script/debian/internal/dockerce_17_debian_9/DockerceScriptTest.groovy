/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9

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
class DockerceScriptTest extends AbstractDockerceScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: """
service "ssh", host: "localhost", socket: "$localhostSocket"
service "docker"
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource DockerceScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource DockerceScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource DockerceScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource DockerceScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource DockerceScriptTest, new File(dir, '/etc/default'), "grub", "${args.test.name}_grub_expected.txt"
                assertFileResource DockerceScriptTest, new File(gen, '/etc/systemd/system/docker.service.d'), "00_dockerd_opts.conf", "${args.test.name}_dockerd_opts_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_mirror_localhost"() {
        def test = [
            name: "script_mirror_localhost",
            script: """
service "ssh", host: "localhost", socket: "$localhostSocket"
service "docker" with {
    registry mirror: 'localhost'
}
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource DockerceScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource DockerceScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource DockerceScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource DockerceScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource DockerceScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource DockerceScriptTest, new File(gen, '/etc/systemd/system/docker.service.d'), "10_mirror.conf", "${args.test.name}_mirror_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_mirror_localhost_ca"() {
        def test = [
            name: "script_mirror_localhost_ca",
            script: """
service "ssh", host: "localhost", socket: "$localhostSocket"
service "docker" with {
    registry mirror: 'localhost', ca: '$certCaPem'
}
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource DockerceScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource DockerceScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource DockerceScriptTest, new File(gen, '/etc/systemd/system/docker.service.d'), "10_mirror.conf", "${args.test.name}_mirror_conf_expected.txt"
                assertFileResource DockerceScriptTest, new File(dir, '/etc/docker/certs.d/localhost:5000'), "ca.crt", "cert_ca.txt"
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
