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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

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
class K8sMasterScriptTest extends AbstractMasterScriptTest {

    @Test
    void "script with interal etcd cluster, default network plugin, default certificates"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "node-0"
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "kubeadm.out", "${args.test.name}_kubeadm_expected.txt"
                assertFileResource K8sMasterScriptTest, gen, "kubeadm.yaml", "${args.test.name}_kubeadm_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script with external etcd cluster, canal network plugin, custom CA certificates"() {
        def test = [
            name: "script_custom_ca_etcd_canal",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "node-0" with {
    ca certs
    plugin "etcd", endpoint: "etcd"
    plugin "canal"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "kubeadm.out", "${args.test.name}_kubeadm_expected.txt"
                assertFileResource K8sMasterScriptTest, gen, "kubeadm.yaml", "${args.test.name}_kubeadm_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_advertise_host"() {
        def test = [
            name: "script_advertise_host",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "node-0", advertise: targets.all[0] with {
    ca certs
    plugin "etcd", endpoint: "etcd"
    plugin "canal"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, gen, "kubeadm.yaml", "${args.test.name}_kubeadm_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "explicit kubelet node address"() {
        def test = [
            name: "script_kubelet_node_address",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-cluster"
service "k8s-master", name: "node-0", advertise: '192.168.0.100' with {
    kubelet address: "192.168.56.200"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_kubelet_labels"() {
        def test = [
            name: "script_kubelet_labels",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-cluster"
service "k8s-master", name: "node-0", advertise: '192.168.0.100' with {
    label << "some-label=value"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_kubelet_tains"() {
        def test = [
            name: "script_kubelet_tains",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-cluster"
service "k8s-master", name: "node-0", advertise: '192.168.0.100' with {
    taint << "dedicated=mail:NoSchedule"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_tls_etcd_tls_canal"() {
        def test = [
            name: "script_tls_etcd_tls_canal",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "node-0", advertise: '192.168.0.100' with {
    ca certs
    plugin "etcd", endpoint: "etcd" with {
        tls certs
    }
    plugin "canal"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource K8sMasterScriptTest, gen, "kubeadm.yaml", "${args.test.name}_kubeadm_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "open ports for master via ufw"() {
        def test = [
            name: "script_master_ufw",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "master-0", advertise: '192.168.0.100' with {
    tls certs
    plugin "etcd", endpoint: "etcd"
    plugin "canal"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            before: { Map test ->
                createEchoCommand test.dir, 'which'
                createCommand ufwActiveCommand, test.dir, 'ufw'
            },
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "open ports between nodes via ufw"() {
        def test = [
            name: "script_nodes_ufw",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "localhost", socket: localhostSocket, group: "nodes"
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "master-0", advertise: '192.168.0.100' with {
    nodes << "default"
    nodes << "nodes"
    tls certs
    plugin "etcd", endpoint: "etcd"
    plugin "canal"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            before: { Map test ->
                createEchoCommand test.dir, 'which'
                createCommand ufwActiveCommand, test.dir, 'ufw'
            },
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_etcd_address_defaults"() {
        def test = [
            name: "script_etcd_address_defaults",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "andrea-cluster", advertise: '192.168.0.100' with {
    plugin "etcd", endpoint: "etcd"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, gen, "kubeadm.yaml", "${args.test.name}_kubeadm_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_etcd_addresses"() {
        def test = [
            name: "script_etcd_addresses",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "k8s-master", name: "andrea-cluster", advertise: '192.168.0.100' with {
    plugin "etcd", endpoint: "https://etcd-0:2379,https://etcd-1:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, gen, "kubeadm.yaml", "${args.test.name}_kubeadm_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "canal iface network"() {
        def test = [
            name: "script_canal_iface_network",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "k8s-master", name: "andrea-cluster", advertise: '192.168.0.100' with {
    plugin "canal", iface: "enp0s8"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "sed.out", "${args.test.name}_sed_expected.txt"
            },
        ]
        doTest test
    }

    //@Test
    void "script_taints_labels"() {
        def test = [
            name: "script_taints_labels",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', ca: certs.ca, cert: certs.cert, key: certs.key
}
service "k8s-master", name: "master-0", advertise: '192.168.0.100' with {
    taint << "dedicated=mail:NoSchedule"
    label << "robobeerun.com/dns=true"
    label << "robobeerun.com/dashboard=true"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
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
