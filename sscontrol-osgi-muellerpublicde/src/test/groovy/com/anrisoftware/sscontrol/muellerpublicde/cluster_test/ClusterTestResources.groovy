/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.muellerpublicde.cluster_test

import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class ClusterTestResources {

    static final int nodesSshPort = 22222

    static final mainNetwork = "enp0s8"

    static final Map socketFiles = [
        masters: [
            "/tmp/robobee@robobee-test:22",
        ],
        nodes: [
            "/tmp/robobee@robobee-1-test:${nodesSshPort}",
            "/tmp/robobee@robobee-2-test:${nodesSshPort}",
        ],
        mails: [
            "/tmp/robobee@andrea-mail-0.muellerpublic.de:22",
        ],
        etcd: [
            "/tmp/robobee@robobee-test:22",
            "/tmp/robobee@robobee-1-test:${nodesSshPort}",
            "/tmp/robobee@robobee-2-test:${nodesSshPort}",
        ],
        nfs: [
            "/tmp/robobee@robobee-test:22",
            "/tmp/robobee@robobee-1-test:${nodesSshPort}",
            "/tmp/robobee@robobee-2-test:${nodesSshPort}",
        ],
    ]

    static final Map socketFiles22 = [
        masters: [
            '/tmp/robobee@robobee-test:22',
        ],
        nodes: [
            '/tmp/robobee@robobee-1-test:22',
            '/tmp/robobee@robobee-2-test:22',
        ],
    ]

    static final List mastersHosts = [
        'robobee-test',
    ]

    static final List nodesHosts = [
        'robobee-1-test',
        'robobee-2-test',
    ]

    static final List mailsHosts = [
        'robobee-test',
    ]

    static final List etcdHosts = [
        'robobee-test',
        'robobee-1-test',
        'robobee-2-test',
    ]

    static final List nfsHosts = [
        'robobee-test',
        'robobee-1-test',
        'robobee-2-test',
    ]

    static final String localRobobeeSocket = "/tmp/${System.getProperty('user.name')}@localhost:22"

    static final URL muellerpublicKey = ClusterTestResources.class.getResource('muellerpublic_de_robobee.txt')

    static final URL etcd_ca = ClusterTestResources.class.getResource('robobee_test_etcd_ca.pem')

    static final Map etcd_vars = [
        client: [
            ca: "$etcd_ca",
            cert: "${ClusterTestResources.class.getResource('robobee_test_etcd_kube_0_cert.pem')}",
            key: "${ClusterTestResources.class.getResource('robobee_test_etcd_kube_0_key.pem')}",
        ],
        nodes: [
            [
                ca: "$etcd_ca",
                cert: "${ClusterTestResources.class.getResource('robobee_test_etcd_etcd_0_server_cert.pem')}",
                key: "${ClusterTestResources.class.getResource('robobee_test_etcd_etcd_0_server_key.pem')}",
            ],
            [
                ca: "$etcd_ca",
                cert: "${ClusterTestResources.class.getResource('robobee_test_etcd_etcd_1_server_cert.pem')}",
                key: "${ClusterTestResources.class.getResource('robobee_test_etcd_etcd_1_server_key.pem')}",
            ],
            [
                ca: "$etcd_ca",
                cert: "${ClusterTestResources.class.getResource('robobee_test_etcd_etcd_2_server_cert.pem')}",
                key: "${ClusterTestResources.class.getResource('robobee_test_etcd_etcd_2_server_key.pem')}",
            ],
        ]
    ]

    static final Map k8s_vars = [
        /**
         * <pre>
         * token=$(kubeadm token generate)
         * kubeadm token create $token --print-join-command --ttl=24h
         * <pre>
         */
        joinCommand: "kubeadm join 192.168.56.200:6443 --token p2a6v1.lpzzgjcdq1miu904 --discovery-token-ca-cert-hash sha256:dd04f8ef36582ea02aa5da2578196dc700ee5486269a84f174f4683f54ab7b41",
        nodes: [
            [
                // node-0
                labels: [
                    "robobeerun.com/nfs=required",
                    "robobeerun.com/prometheus=required",
                    "robobeerun.com/mariadb-master=required",
                    "robobeerun.com/mariadb-slave=required",
                    "robobeerun.com/postgres-master=required",
                    "robobeerun.com/postgres-slave=required",
                    "robobeerun.com/keycloak=required",
                ],
                taints: [],
            ],
            [
                // node-1
                labels: [
                    "robobeerun.com/ingress-nginx=required",
                    "robobeerun.com/cert-manager=required",
                    "robobeerun.com/grafana=required",
                    "robobeerun.com/prometheus=required",
                    "robobeerun.com/mariadb-master=required",
                    "robobeerun.com/mariadb-slave=required",
                    "robobeerun.com/postgres-master=required",
                    "robobeerun.com/postgres-slave=required",
                    "www.interscalar.com=required",
                    "www.muellerpublic.de=required",
                ],
                taints: [],
            ],
            [
                // node-2
                labels: [
                    "robobeerun.com/prometheus=required",
                    "robobeerun.com/mariadb-master=required",
                    "robobeerun.com/mariadb-slave=required",
                    "robobeerun.com/postgres-master=required",
                    "robobeerun.com/postgres-slave=required",
                    "project.anrisoftware.com=required"
                ],
                taints: [],
            ],
        ],
        etcd: [
            certs:[
                ca: "$etcd_ca",
                cert: "${ClusterTestResources.class.getResource('robobee_test_etcd_kube_0_cert.pem')}",
                key: "${ClusterTestResources.class.getResource('robobee_test_etcd_kube_0_key.pem')}",
            ]
        ],
        rsync: [
            key: ClusterTestResources.class.getResource('rsync_id_rsa.txt'),
            publicKey: ClusterTestResources.class.getResource('rsync_id_rsa_pub.txt'),
        ]
    ]
}
