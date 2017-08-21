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
class EtcdServerTest extends AbstractEtcdRunnerTest {

    @Test
    void "etcd_script_basic_server"() {
        def test = [
            name: "etcd_script_basic_server",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "etcd", member: "default"
''',
            scriptVars: [robobeeSocket: robobeeSocket, certs: andreaLocalEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd.conf'), "${args.test.name}_etcd_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd.service'), "${args.test.name}_etcd_service_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "etcd_tls_peers_server"() {
        def test = [
            name: "etcd_tls_peers_server",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
def host = targets['all'][0]
def i = 0
service "etcd", target: host, member: "etcd-\${i}" with {
    debug "debug", level: 1
    bind "https://\${host.hostAddress}:2379"
    advertise "https://robobee-test:2379"
    tls cert: certs.etcd[i].cert, key: certs.etcd[i].key
    authentication "cert", ca: certs.ca
    peer state: "new", advertise: "https://robobee-test:2380", listen: "https://\${host.hostAddress}:2380", token: "robobee-test-cluster-1" with {
        cluster << "etcd-0=https://robobee-test:2380"
        tls cert: certs.etcd[i].cert, key: certs.etcd[i].key
        authentication "cert", ca: certs.ca
    }
}
''',
            scriptVars: [robobeeSocket: robobeeSocket, certs: andreaLocalEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd.conf'), "${args.test.name}_etcd_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd.service'), "${args.test.name}_etcd_service_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFilesPrivileged('/etc/etcd/ssl'), "${args.test.name}_ssl_expected.txt"
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
        getEmptyScriptEnv(args)
    }
}
