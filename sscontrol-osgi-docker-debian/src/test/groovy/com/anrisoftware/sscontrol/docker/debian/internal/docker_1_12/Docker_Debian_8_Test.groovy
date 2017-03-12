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
package com.anrisoftware.sscontrol.docker.debian.internal.docker_1_12

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
class Docker_Debian_8_Test extends AbstractTest_Docker_Debian_8 {

    @Test
    void "basic"() {
        def test = [
            name: "basic",
            input: """
service "ssh", host: "localhost"
service "docker"
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Docker_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource Docker_Debian_8_Test, new File(dir, '/etc/default'), "grub", "${args.test.name}_grub_expected.txt"
                assertFileResource Docker_Debian_8_Test, new File(gen, '/etc/systemd/system/docker.service.d'), "00_dockerd_opts.conf", "${args.test.name}_dockerd_opts_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "mirror_localhost"() {
        def test = [
            name: "mirror_localhost",
            input: """
service "ssh", host: "localhost"
service "docker" with {
    registry mirror: 'localhost'
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Docker_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource Docker_Debian_8_Test, new File(gen, '/etc/systemd/system/docker.service.d'), "10_mirror.conf", "${args.test.name}_mirror_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "mirror_localhost_ca"() {
        def test = [
            name: "mirror_localhost_ca",
            input: """
service "ssh", host: "localhost"
service "docker" with {
    registry mirror: 'localhost', ca: '$certCaPem'
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Docker_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource Docker_Debian_8_Test, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource Docker_Debian_8_Test, new File(gen, '/etc/systemd/system/docker.service.d'), "10_mirror.conf", "${args.test.name}_mirror_conf_expected.txt"
                assertFileResource Docker_Debian_8_Test, new File(dir, '/etc/docker/certs.d/localhost:5000'), "ca.crt", "cert_ca.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }
}
