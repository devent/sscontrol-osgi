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
package com.anrisoftware.sscontrol.k8snode.debian.internal.k8snode_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class K8sNode_Debian_8_Test extends AbstractTest_K8sNode_Debian_8 {

    @Test
    void "tls_api_host"() {
        def test = [
            name: "tls_api_host",
            input: """
service "ssh", host: "localhost"
service "k8s-node", name: "andrea-cluster", advertise: '192.168.0.200', api: 'https://192.168.0.100' with {
    tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    }
}
""",
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
            input: """
service "ssh", group: "master" with {
    host "master.robobee.test"
}
service "ssh", group: "nodes" with {
    host "localhost"
}
service "k8s-node", name: "andrea-cluster", target: "nodes", advertise: '192.168.0.200', api: targets['master'] with {
    tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    }
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sNode_Debian_8_Test, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
            },
        ]
        doTest test
    }
}
