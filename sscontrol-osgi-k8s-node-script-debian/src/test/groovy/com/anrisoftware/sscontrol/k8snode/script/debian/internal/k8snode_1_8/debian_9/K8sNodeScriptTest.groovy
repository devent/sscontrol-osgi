package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*
import static org.junit.jupiter.api.Assertions.assertThrows

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class K8sNodeScriptTest extends AbstractNodeScriptTest {

    @Test
    void "script_no_join"() {
        def test = [
            name: "script_no_join",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-node", name: "node-0"
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
            },
        ]
        assertThrows AssertionError.class, { doTest test }
    }

    @Test
    void "script_join_arg"() {
        def test = [
            name: "script_join_arg",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "localhost", socket: localhostSocket, group: "masters"
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-cluster", target: "masters"
service "k8s-node", name: "node-0", join: 'kubeadm join abc'
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNodeScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource K8sNodeScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sNodeScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sNodeScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sNodeScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sNodeScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource K8sNodeScriptTest, dir, "kubeadm.out", "${args.test.name}_kubeadm_expected.txt"
                assertFileResource K8sNodeScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
                //assertFileResource K8sNodeScriptTest, dir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
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
service "k8s-node", name: "node-0", join: 'kubeadm join abc' with {
    label << "some-label=value"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNodeScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "open ports for node via ufw"() {
        def test = [
            name: "script_nodes_ufw",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-node", name: "node-0", join: 'kubeadm join abc'
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
                assertFileResource K8sNodeScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
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
service "k8s-node", name: "node-0", join: 'kubeadm join abc' with {
    kubelet address: "192.168.56.200"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNodeScriptTest, new File(gen, "kubelet.service.d"), "20-robobee.conf", "${args.test.name}_kubelet_extra_conf_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
