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

import static com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@EnabledIfSystemProperty(named = 'project.custom.local.tests.enabled', matches = 'true')
@ExtendWith(LocalhostSocketCondition.class)
class EtcdScriptTest extends AbstractEtcdScriptTest {

    @Test
    void "script_basic_etcd"() {
        def test = [
            name: "script_basic_etcd",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default"
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource EtcdScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                try {
                    assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl1_expected.txt"
                } catch (AssertionError e) {
                    assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl2_expected.txt"
                }
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd.service", "${args.test.name}_etcd_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd.conf", "${args.test.name}_etcd_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_tls_basic"() {
        def test = [
            name: "script_tls_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default" with {
    bind "http://localhost:2379"
    bind "https://etcd-0.muellerpublic.de:2379"
    advertise "https://etcd-0.muellerpublic.de:2379"
    tls testTlsCerts
    client testClientCerts
}
''',
            scriptVars: [localhostSocket: localhostSocket, testTlsCerts: testTlsCerts, testClientCerts: testClientCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd.service", "${args.test.name}_etcd_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd.conf", "${args.test.name}_etcd_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_cert_auth"() {
        def test = [
            name: "script_cert_auth",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default" with {
    authentication "cert", ca: testTlsCerts.ca
}
''',
            scriptVars: [localhostSocket: localhostSocket, testTlsCerts: testTlsCerts],
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
    void "script_static_peer"() {
        def test = [
            name: "script_static_peer",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "infra0" with {
    peer state: "new", advertise: "https://10.0.1.10:2380", listen: "https://10.0.1.10:2380", token: "etcd-cluster-1" with {
        cluster << "infra0=https://10.0.1.10:2380"
        cluster name: "infra1", address: "https://10.0.1.11:2380"
        cluster "infra2", address: "https://10.0.1.12:2380"
        tls testTlsCerts
        authentication "cert", ca: testTlsCerts.ca
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket, testTlsCerts: testTlsCerts],
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
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_peers_ufw"() {
        def test = [
            name: "script_peers_ufw",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "infra0" with {
    peer state: "new", advertise: "https://10.0.1.10:2380", listen: "https://10.0.1.10:2380", token: "etcd-cluster-1" with {
        cluster << "infra0=https://10.0.1.10:2380"
        cluster name: "infra1", address: "https://10.0.1.11:2380"
        cluster "infra2", address: "https://10.0.1.12:2380"
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            before: { Map test ->
                createEchoCommand test.dir, 'which'
                createCommand EtcdScriptTest.class.getResource("peers_ufw_ufw_cmd.txt"), test.dir, 'ufw'
            },
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_basic_proxy"() {
        def test = [
            name: "script_basic_proxy",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd" with {
    proxy endpoints: "http://etcd-0:2379,http://etcd-1:2379,http://etcd-2:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                try {
                    assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                } catch (AssertionError e) {
                    assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_alternative_expected.txt"
                }
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd-proxy.service", "${args.test.name}_etcd_proxy_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd-proxy.conf", "${args.test.name}_etcd_proxy_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_tls_proxy"() {
        def test = [
            name: "script_tls_proxy",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd" with {
    proxy endpoints: "https://etcd-0:2379,https://etcd-1:2379,https://etcd-2:2379"
    client testClientCerts
}
''',
            scriptVars: [localhostSocket: localhostSocket, testClientCerts: testClientCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd-proxy.service", "${args.test.name}_etcd_proxy_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd-proxy.conf", "${args.test.name}_etcd_proxy_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_basic_gateway"() {
        def test = [
            name: "script_basic_gateway",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd" with {
    bind "http://localhost:22379"
    gateway endpoints: "http://etcd-0:2379,http://etcd-1:2379,http://etcd-2:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                try {
                    assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                } catch (AssertionError e) {
                    assertFileResource EtcdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_alternative_expected.txt"
                }
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd-gateway.service", "${args.test.name}_etcd_gateway_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd-gateway.conf", "${args.test.name}_etcd_gateway_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_tls_gateway"() {
        def test = [
            name: "script_tls_gateway",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd" with {
    bind "https://127.0.0.1:22379"
    gateway endpoints: "https://etcd-0:2379,https://etcd-1:2379,https://etcd-2:2379"
    client testClientCerts
}
''',
            scriptVars: [localhostSocket: localhostSocket, testClientCerts: testClientCerts],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd-gateway.service", "${args.test.name}_etcd_gateway_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd-gateway.conf", "${args.test.name}_etcd_gateway_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_vnetwork_gateway"() {
        def test = [
            name: "script_vnetwork_gateway",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd" with {
    bind network: "enp0s8:1", "http://10.10.10.7:22379"
    gateway endpoints: "http://etcd-0:2379,http://etcd-1:2379,http://etcd-2:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            before: { Map test ->
                def dir = new File(test.dir, '/etc/network')
                dir.mkdirs()
                createFile EtcdScriptTest.class.getResource("debian_network_interfaces.txt"), dir, 'interfaces'
            },
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/systemd/system'), "etcd-gateway.service", "${args.test.name}_etcd_gateway_service_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/etc/etcd'), "etcd-gateway.conf", "${args.test.name}_etcd_gateway_config_expected.txt"
                assertFileResource EtcdScriptTest, new File(gen, '/usr/local/share/'), "etcdctl-vars", "${args.test.name}_etcdctl_vars_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_check_on"() {
        def test = [
            name: "script_check_on",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "etcd", member: "default" with {
    check on: targets[0]
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource EtcdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource EtcdScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource EtcdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource EtcdScriptTest, dir, "etcdctl.out", "${args.test.name}_etcdctl_expected.txt"
            },
        ]
        doTest test
    }
}
