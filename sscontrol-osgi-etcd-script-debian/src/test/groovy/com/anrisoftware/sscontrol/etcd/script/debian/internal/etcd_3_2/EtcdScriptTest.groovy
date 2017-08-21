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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.etcd_3_2

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
class EtcdScriptTest extends AbstractEtcdScriptTest {

    @Test
    void "basic"() {
        def test = [
            name: "basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default"
''',
            scriptVars: [localhostSocket: localhostSocket, testCerts: testCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource EtcdScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd.service", "${args.test.name}_etcd_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd.conf", "${args.test.name}_etcd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "tls"() {
        def test = [
            name: "tls",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default" with {
    bind "http://localhost:2379"
    bind "https://etcd-0.muellerpublic.de:2379"
    advertise "https://etcd-0.muellerpublic.de:2379"
    tls testCerts
}
''',
            scriptVars: [localhostSocket: localhostSocket, testCerts: testCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd.service", "${args.test.name}_etcd_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd.conf", "${args.test.name}_etcd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "cert_auth"() {
        def test = [
            name: "cert_auth",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default" with {
    authentication "cert", ca: testCerts.ca
}
''',
            scriptVars: [localhostSocket: localhostSocket, testCerts: testCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd.service", "${args.test.name}_etcd_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd.conf", "${args.test.name}_etcd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "static_peer"() {
        def test = [
            name: "static_peer",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "infra0" with {
    peer state: "new", advertise: "https://10.0.1.10:2380", listen: "https://10.0.1.10:2380", token: "etcd-cluster-1" with {
        cluster << "infra0=https://10.0.1.10:2380"
        cluster name: "infra1", address: "https://10.0.1.11:2380"
        cluster "infra2", address: "https://10.0.1.12:2380"
        tls testCerts
        authentication "cert", ca: testCerts.ca
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket, testCerts: testCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd.service", "${args.test.name}_etcd_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd.conf", "${args.test.name}_etcd_config_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        //checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
