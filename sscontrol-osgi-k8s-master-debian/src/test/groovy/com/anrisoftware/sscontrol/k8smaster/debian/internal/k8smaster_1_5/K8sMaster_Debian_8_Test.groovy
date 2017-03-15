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
package com.anrisoftware.sscontrol.k8smaster.debian.internal.k8smaster_1_5

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
class K8sMaster_Debian_8_Test extends AbstractTest_K8sMaster_Debian_8 {

    @Test
    void "tls_etcd_target"() {
        def test = [
            name: "tls_etcd_target",
            input: """
service "ssh", host: "localhost"
service "ssh", host: "localhost", group: "etcd"
service "k8s-master", name: "andrea-cluster", advertise: '192.168.0.100' with {
    tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    authentication "cert", ca: "$certCaPem"
    plugin "etcd", target: "etcd"
    kubelet.with {
        tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    }
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/systemd/system'), "kubelet.service", "${args.test.name}_kubelet_service_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/usr/local/bin'), "host-rkt", "${args.test.name}_host_rkt_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yml", "${args.test.name}_kube_proxy_yml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/kubernetes/manifests'), "kube-apiserver.yml", "${args.test.name}_kube_apiserver_yml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/kubernetes/manifests'), "kube-controller-manager.yml", "${args.test.name}_kube_controller_manager_yml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "kube-dns-de.yml", "${args.test.name}_kube_dns_de_yml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "kube-dns-autoscaler-de.yml", "${args.test.name}_kube_dns_autoscaler_de_yml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "kube-dns-svc.yml", "${args.test.name}_kube_dns_svc_yml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "heapster-de.yaml", "${args.test.name}_heapster_de_yaml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "heapster-svc.yaml", "${args.test.name}_heapster_svc_yaml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "kube-dashboard-de.yaml", "${args.test.name}_kube_dashboard_de_yaml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "kube-dashboard-svc.yaml", "${args.test.name}_kube_dashboard_svc_yaml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/srv/kubernetes/manifests'), "calico.yaml", "${args.test.name}_calico_yaml_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "etcd_address_defaults"() {
        def test = [
            name: "etcd_address_defaults",
            input: """
service "ssh", host: "localhost"
service "k8s-master", name: "andrea-cluster" with {
    plugin "etcd", address: "etcd"
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/kubernetes'), "apiserver", "${args.test.name}_kubernetes_apiserver_config_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "etcd_tls"() {
        def test = [
            name: "etcd_tls",
            input: """
service "ssh", host: "localhost"
service "k8s-master", name: "andrea-cluster" with {
    plugin "etcd", address: "etcd" with {
        tls ca: "$certCaPem", cert: "$certCertPem", key: "$certKeyPem"
    }
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMaster_Debian_8_Test, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/systemd/system'), "kube-apiserver.service", "${args.test.name}_kube_apiserver_service_expected.txt"
                assertFileResource K8sMaster_Debian_8_Test, new File(gen, '/etc/kubernetes'), "apiserver", "${args.test.name}_kubernetes_apiserver_config_expected.txt"
            },
        ]
        doTest test
    }
}
