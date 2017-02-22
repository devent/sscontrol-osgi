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
package com.anrisoftware.sscontrol.flanneldocker.debian.internal.flanneldocker_0_7

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
class FlannelDocker_0_7_Debian_8_Test extends AbstractTest_FlannelDocker_Debian_8 {

    @Test
    void "basic"() {
        def test = [
            name: "basic",
            input: """
service "ssh", host: "localhost"
service "flannel-docker" with {
    etcd "http://127.0.0.1:2379"
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource FlannelDocker_0_7_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                //assertFileResource FlannelDocker_0_7_Debian_8_Test, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, new File(gen, '/etc/systemd/system'), "flanneld.service", "${args.test.name}_flanneld_service_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, new File(gen, '/etc/systemd/tmpfiles.d'), "flannel.conf", "${args.test.name}_flanneld_tmpfiles_config_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, new File(gen, '/lib/systemd/system/docker.service.d'), "flannel.conf", "${args.test.name}_flannel_docker_conf_expected.txt"
                assertFileResource FlannelDocker_0_7_Debian_8_Test, new File(gen, '/etc/sysconfig'), "flanneld", "${args.test.name}_flanneld_sysconfig_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }
}
