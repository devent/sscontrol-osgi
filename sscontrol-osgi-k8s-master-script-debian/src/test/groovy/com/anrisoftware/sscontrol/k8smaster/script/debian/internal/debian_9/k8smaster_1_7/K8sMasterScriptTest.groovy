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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7

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
class K8sMasterScriptTest extends AbstractMasterScriptTest {

    @Test
    void "script_tls_etcd_target"() {
        def test = [
            name: "script_tls_etcd_target",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "etcd-0", socket: localhostSocket, group: "etcd"
service "k8s-master", name: "master-0", advertise: '192.168.0.100' with {
    tls certs
    authentication "cert", ca: certs.ca
    plugin "etcd", target: "etcd"
    plugin "flannel"
    plugin "calico"
    kubelet.with {
        tls certs
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/systemd/system'), "kubelet.service", "${args.test.name}_kubelet_service_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/systemd/system/docker.service.d'), "10_kube_options.conf", "${args.test.name}_kube_options_conf_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/usr/local/bin'), "host-rkt", "${args.test.name}_host_rkt_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/usr/local/bin'), "kubelet-wrapper", "${args.test.name}_kubelet_wrapper_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-apiserver.yaml", "${args.test.name}_kube_apiserver_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-controller-manager.yaml", "${args.test.name}_kube_controller_manager_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-scheduler.yaml", "${args.test.name}_kube_scheduler_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "kube-dns-de.yaml", "${args.test.name}_kube_dns_de_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "kube-dns-autoscaler-de.yaml", "${args.test.name}_kube_dns_autoscaler_de_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "kube-dns-svc.yaml", "${args.test.name}_kube_dns_svc_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "kube-dashboard-de.yaml", "${args.test.name}_kube_dashboard_de_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "kube-dashboard-svc.yaml", "${args.test.name}_kube_dashboard_svc_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "calico.yaml", "${args.test.name}_calico_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/cni/net.d'), "10-flannel.conf", "${args.test.name}_cni_flannel_conf_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                try {
                    assertFileResource K8sMasterScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl1_expected.txt"
                } catch (AssertionError e) {
                    try {
                        assertFileResource K8sMasterScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl2_expected.txt"
                    }
                    catch (AssertionError e1) {
                        try {
                            assertFileResource K8sMasterScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl3_expected.txt"
                        }
                        catch (AssertionError e2) {
                            assertFileResource K8sMasterScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl4_expected.txt"
                        }
                    }
                }
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
service "k8s-master", name: "andrea-cluster", advertise: '192.168.0.100' with {
    plugin "etcd", address: "etcd"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/systemd/system'), "kubelet.service", "${args.test.name}_kubelet_service_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/sysconfig'), "kubelet", "${args.test.name}_kubelet_conf_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-apiserver.yaml", "${args.test.name}_kube_apiserver_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-controller-manager.yaml", "${args.test.name}_kube_controller_manager_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-scheduler.yaml", "${args.test.name}_kube_scheduler_yaml_expected.txt"
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
    plugin "etcd", address: "https://etcd-0:2379,https://etcd-1:2379"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-apiserver.yaml", "${args.test.name}_kube_apiserver_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-controller-manager.yaml", "${args.test.name}_kube_controller_manager_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-scheduler.yaml", "${args.test.name}_kube_scheduler_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_etcd_tls"() {
        def test = [
            name: "script_etcd_tls",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "k8s-master", name: "andrea-cluster", advertise: '192.168.0.100' with {
    plugin "etcd", address: "etcd" with {
        tls certs
    }
    plugin "calico"
}
''',
            scriptVars: [localhostSocket: localhostSocket, certs: certs],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource K8sMasterScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-apiserver.yaml", "${args.test.name}_kube_apiserver_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-controller-manager.yaml", "${args.test.name}_kube_controller_manager_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-scheduler.yaml", "${args.test.name}_kube_scheduler_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/srv/kubernetes/manifests'), "calico.yaml", "${args.test.name}_calico_yaml_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_advertise_target"() {
        def test = [
            name: "script_advertise_target",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "ssh", host: "localhost", socket: localhostSocket, group: "master"
service "k8s-master", name: "andrea-cluster", advertise: targets['master'][0] with {
    plugin "etcd", address: "etcd"
    plugin "calico"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-proxy.yaml", "${args.test.name}_kube_proxy_yaml_expected.txt"
                assertFileResource K8sMasterScriptTest, new File(gen, '/etc/kubernetes/manifests'), "kube-apiserver.yaml", "${args.test.name}_kube_apiserver_yaml_expected.txt"
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
                //assertFileResource K8sMasterScriptTest, dir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
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
