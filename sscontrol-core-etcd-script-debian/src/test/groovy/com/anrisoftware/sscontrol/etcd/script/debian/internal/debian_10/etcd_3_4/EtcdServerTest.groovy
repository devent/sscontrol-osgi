/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_10.etcd_3_4

import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(RobobeeSocketCondition.class)
class EtcdServerTest extends AbstractEtcdRunnerTest {

    @Test
    void "server_basic_etcd"() {
        def test = [
            name: "server_basic_etcd",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "etcd", member: "default"
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd.conf'), "${args.test.name}_etcd_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd.service'), "${args.test.name}_etcd_service_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/usr/local/share/etcdctl-vars'), "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_basic_proxy"() {
        def test = [
            name: "server_basic_proxy",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "etcd" with {
    bind "http://localhost:12379"
    proxy endpoints: "http://localhost:2379"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd-proxy.conf'), "${args.test.name}_etcd_proxy_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd-proxy.service'), "${args.test.name}_etcd_proxy_service_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/usr/local/share/etcdctl-vars'), "${args.test.name}_etcdctl_vars_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, remoteCommand('netstat -lnp|grep 12379').replaceAll('\\d+\\/etcd', 'id/etcd').replaceAll('\\s*$', ''), "${args.test.name}_netstat_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_basic_gateway"() {
        def test = [
            name: "server_basic_gateway",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "etcd" with {
    bind "http://localhost:22379"
    gateway endpoints: "http://localhost:2379"
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd-gateway.conf'), "${args.test.name}_etcd_gateway_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd-gateway.service'), "${args.test.name}_etcd_gateway_service_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/usr/local/share/etcdctl-vars'), "${args.test.name}_etcdctl_vars_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, remoteCommand('netstat -lnp|grep 22379').replaceAll('\\d+\\/etcd', 'id/etcd').replaceAll('\\s*$', ''), "${args.test.name}_netstat_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_tls_peers"() {
        def test = [
            name: "server_tls_peers",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
def host = targets['all'][0]
def i = 0
service "etcd", target: host, member: "etcd-${i}" with {
    debug "debug", level: 1
    bind "https://${host.hostAddress}:2379"
    advertise "https://etcd-${i}.robobee-test.test:2379"
    client certs.client
    tls cert: certs.etcd[i].cert, key: certs.etcd[i].key
    authentication "cert", ca: certs.ca
    peer state: "new", advertise: "https://etcd-${i}.robobee-test.test:2380", listen: "https://${host.hostAddress}:2380", token: "robobee-test-cluster-1" with {
        cluster << "etcd-0=https://etcd-${i}.robobee-test.test:2380"
        tls cert: certs.etcd[i].cert, key: certs.etcd[i].key
        authentication "cert", ca: certs.ca
    }
}
''',
            scriptVars: [robobeeSocket: robobeeSocket, certs: robobeetestEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd.conf'), "${args.test.name}_etcd_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd.service'), "${args.test.name}_etcd_service_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFilesPrivileged('/etc/etcd/ssl'), "${args.test.name}_ssl_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/usr/local/share/etcdctl-vars'), "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_tls_proxy"() {
        def test = [
            name: "server_tls_proxy",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "etcd" with {
    bind "http://localhost:12379"
    proxy endpoints: "https://etcd-0.robobee-test.test:2379"
    client certs.proxy
}
''',
            scriptVars: [robobeeSocket: robobeeSocket, certs: robobeetestEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd-proxy.conf'), "${args.test.name}_etcd_proxy_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd-proxy.service'), "${args.test.name}_etcd_proxy_service_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/usr/local/share/etcdctl-vars'), "${args.test.name}_etcdctl_vars_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, remoteCommand('netstat -lnp|grep 127.0.0.1:12379').replaceAll('\\d+\\/etcd', 'id/etcd').replaceAll('\\s*$', ''), "${args.test.name}_netstat_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "server_tls_gateway"() {
        def test = [
            name: "server_tls_gateway",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
def host = targets['all'][0]
service "etcd" with {
    bind "https://${host.hostAddress}:22379"
    gateway endpoints: "https://etcd-0.robobee-test.test:2379"
    client certs.client
}
''',
            scriptVars: [robobeeSocket: robobeeSocket, certs: robobeetestEtcdCerts],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource EtcdServerTest, readRemoteFile('/etc/etcd/etcd-gateway.conf'), "${args.test.name}_etcd_gateway_conf_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/etc/systemd/system/etcd-gateway.service'), "${args.test.name}_etcd_gateway_service_expected.txt"
                assertStringResource EtcdServerTest, readRemoteFile('/usr/local/share/etcdctl-vars'), "${args.test.name}_etcdctl_vars_expected.txt"
                assertStringResource EtcdServerTest, checkRemoteFiles('/usr/local/bin/etcd*'), "${args.test.name}_bins_expected.txt"
                assertStringResource EtcdServerTest, remoteCommand('netstat -lnp|grep :22379').replaceAll('\\d+\\/etcd', 'id/etcd').replaceAll('\\s*$', ''), "${args.test.name}_netstat_expected.txt"
            },
        ]
        doTest test
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
