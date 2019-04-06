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
        joinCommand: "kubeadm join --token 733471.e9d3a2f2370e0915 185.24.220.41:6443 --discovery-token-ca-cert-hash sha256:2c18995abbf9ba44f93d7b8c364c447bc3473abe7b372a11b82c0a9a9dadae70",
        nodes: [
            [
                labels: [
                    "robobeerun.com/heapster=required",
                    "robobeerun.com/dashboard=required"
                ],
                taints: [],
            ],
            [
                labels: [
                    "robobeerun.com/edge-router=required",
                    "muellerpublic.de/interscalar-com=required",
                    "robobeerun.com/cert-manager=required",
                    "robobeerun.com/ingress-nginx=required",
                ],
                taints: [],
            ],
            [
                labels: [
                    "muellerpublic.de/anrisoftware-com=required"
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
