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
        joinCommand: "kubeadm join 192.168.56.200:6443 --token boaoo6.e1p1ofzfs7p3p8k2 --discovery-token-ca-cert-hash sha256:48f187ff659cd3add6fed41814f4c557bf74dfd56073c47a8be339f3d14b375e",
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
            key: "/com/anrisoftware/sscontrol/muellerpublicde/rsync_id_rsa.txt",
            publicKey: "/com/anrisoftware/sscontrol/muellerpublicde/rsync_id_rsa_pub.txt",
        ]
    ]

    static final postgresVars = [
        image: [name: "bitnami/postgresql", version: "10.7.0-debian-9-r57"],
        admin: [user: "postgres", password: "ochae6chiez5Wicheb8OiKooVeequu6o"],
        host: "robobeerun-com-postgres-postgresql.robobeerun-com-postgres.svc",
        port: 5432
    ]

    static final pgbouncerVars = [
        image: [name: "crunchydata/crunchy-pgbouncer", version: "centos7-10.7-2.3.1"],
    ]

    static final keycloakVars = [
        hosts: ["keycloak.robobee.test"]]

    static final mariadbVars = [
        image: [name: "bitnami/mariadb", version: "10.1.38"],
        admin: [user: "root", password: "wooSh6ohrah2AiCiY7uchoh5Leisee4x"],
        host: "robobeerun-com-mariadb.robobeerun-com-mariadb.svc",
        port: 3306
    ]

}
