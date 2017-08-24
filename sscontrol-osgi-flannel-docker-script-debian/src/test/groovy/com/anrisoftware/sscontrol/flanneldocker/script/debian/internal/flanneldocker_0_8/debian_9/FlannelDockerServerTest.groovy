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
package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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
class FlannelDockerServerTest extends AbstractFlannelDockerRunnerTest {

    @Test
    void "server_basic"() {
        def test = [
            name: "server_basic",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "flannel-docker" with {
    etcd "http://127.0.0.1:2379"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource FlannelDockerServerTest, readRemoteFile('/etc/systemd/system/flanneld.service'), "${args.test.name}_flanneld_service_expected.txt"
                assertStringResource FlannelDockerServerTest, readRemoteFile('/etc/systemd/tmpfiles.d/flannel.conf'), "${args.test.name}_flannel_tmpfiles_conf_expected.txt"
                assertStringResource FlannelDockerServerTest, readRemoteFile('/lib/systemd/system/docker.service'), "${args.test.name}_docker_service_expected.txt"
                assertStringResource FlannelDockerServerTest, readRemoteFile('/etc/sysconfig/flanneld'), "${args.test.name}_flanneld_expected.txt"
                assertStringResource FlannelDockerServerTest, readRemoteFile('/etc/systemd/system/docker.service.d/10_flannel.conf'), "${args.test.name}_flannel_docker_conf_expected.txt"
                assertStringResource FlannelDockerServerTest, checkRemoteFiles('/usr/local/bin/flannel*'), "${args.test.name}_local_bin_expected.txt"
                assertStringResource FlannelDockerServerTest, checkRemoteFiles('/usr/libexec/flannel'), "${args.test.name}_libexec_flannel_expected.txt"
                def s = checkRemoteFiles('/run/flannel')
                s = s.replaceAll('\\d{2,3}', 'n')
                assertStringResource FlannelDockerServerTest, s, "${args.test.name}_run_flannel_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
        assumeTrue testHostAvailable
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}