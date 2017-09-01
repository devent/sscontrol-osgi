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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_5

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
class K8sNode_Debian_8_Test extends AbstractNodeScriptTest {

    @Test
    void "tls_api_host"() {
        def test = [
            name: "tls_api_host",
            script: """
service "ssh", host: "localhost", socket: "$localhostSocket"
service "k8s-node", name: "andrea-cluster", advertise: '192.168.0.200', api: 'https://192.168.0.100', cluster: "default" with {
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls cert: "$certCertPem", key: "$certKeyPem"
        client ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    }
}
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/systemd/system'), "kubelet.service", "${args.test.name}_kubelet_service_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/usr/local/bin'), "host-rkt", "${args.test.name}_host_rkt_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/usr/local/bin'), "kubelet-wrapper", "${args.test.name}_kubelet_wrapper_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/kubernetes/cni/net.d'), "10-flannel.conf", "${args.test.name}_cni_flannel_conf_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/kubernetes'), "worker-kubeconfig.yaml", "${args.test.name}_worker_kubeconfig_yaml_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "tls_api_target"() {
        def test = [
            name: "tls_api_target",
            script: """
service "ssh", group: "master" with {
    host "master.robobee.test", socket: "$localhostSocket"
}
service "ssh", group: "nodes" with {
    host "localhost"
}
service "k8s-node", name: "andrea-cluster", target: "nodes", advertise: '192.168.0.200', api: targets['master'] with {
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls cert: "$certCertPem", key: "$certKeyPem"
        client ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    }
}
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/systemd/system'), "kubelet.service", "${args.test.name}_kubelet_service_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "taints"() {
        def test = [
            name: "taints",
            script: """
service "ssh", group: "master" with {
    host "master.robobee.test", socket: "$localhostSocket"
}
service "ssh", group: "nodes" with {
    host "localhost"
}
service "k8s-cluster", target: "master" with {
    credentials type: 'cert', name: 'default-admin', ca: '$certCaPem', cert: '$certCertPem', key: '$certKeyPem'
}
service "k8s-node", name: "andrea-cluster", target: "nodes", advertise: '192.168.0.200', api: targets['master'] with {
    plugin "flannel"
    plugin "calico"
    taint << "robobeerun.com/dedicated=:NoSchedule"
}
""",
            expectedServicesSize: 3,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/systemd/system'), "kubelet.service", "${args.test.name}_kubelet_service_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, dir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
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
