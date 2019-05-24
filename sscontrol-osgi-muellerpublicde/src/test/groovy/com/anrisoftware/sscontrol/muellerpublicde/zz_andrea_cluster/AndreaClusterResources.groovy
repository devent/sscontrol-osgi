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
package com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster

import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class AndreaClusterResources {

    static final int nodesSshPort = 22222

    static final mainNetwork = "ens3"

    static final Map socketFiles = [
        masters: [
            "/tmp/robobee@andrea-node-0.muellerpublic.de:22",
        ],
        nodes: [
            "/tmp/robobee@andrea-node-1.muellerpublic.de:${nodesSshPort}",
            "/tmp/robobee@andrea-node-2.muellerpublic.de:${nodesSshPort}",
        ],
        mails: [
            "/tmp/robobee@andrea-mail-0.muellerpublic.de:22",
        ],
        etcd: [
            "/tmp/robobee@andrea-node-0.muellerpublic.de:22",
            "/tmp/robobee@andrea-node-1.muellerpublic.de:${nodesSshPort}",
            "/tmp/robobee@andrea-node-2.muellerpublic.de:${nodesSshPort}",
        ],
        nfs: [
            "/tmp/robobee@andrea-node-0.muellerpublic.de:22",
            "/tmp/robobee@andrea-node-1.muellerpublic.de:${nodesSshPort}",
            "/tmp/robobee@andrea-node-2.muellerpublic.de:${nodesSshPort}",
        ],
    ]

    static final Map socketFiles22 = [
        masters: [
            '/tmp/robobee@andrea-node-0.muellerpublic.de:22',
        ],
        nodes: [
            '/tmp/robobee@andrea-node-1.muellerpublic.de:22',
            '/tmp/robobee@andrea-node-2.muellerpublic.de:22',
        ],
    ]

    static final List mastersHosts = [
        'andrea-node-0.muellerpublic.de',
    ]

    static final List nodesHosts = [
        'andrea-node-1.muellerpublic.de',
        'andrea-node-2.muellerpublic.de',
    ]

    static final List mailsHosts = [
        'andrea-node-0.muellerpublic.de',
    ]

    static final List etcdHosts = [
        'etcd-0.muellerpublic.de',
        'etcd-1.muellerpublic.de',
        'etcd-2.muellerpublic.de',
    ]

    static final List nfsHosts = [
        'andrea-mail-0.muellerpublic.de',
    ]

    static final String nfsServer = "andrea-mail-0.muellerpublic.de"

    static final String localRobobeeSocket = "/tmp/${System.getProperty('user.name')}@localhost:22"

    static final URL muellerpublicKey = AndreaClusterResources.class.getResource('muellerpublic_de_robobee.txt')

    static final URL etcd_ca = AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_ca_cert.pem')

    static final Map etcd_vars = [
        client: [
            ca: "$etcd_ca",
            cert: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_cert.pem')}",
            key: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_key.pem')}",
        ],
        nodes: [
            [
                ca: "$etcd_ca",
                cert: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_0_cert.pem')}",
                key: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_0_key.pem')}",
            ],
            [
                ca: "$etcd_ca",
                cert: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_1_cert.pem')}",
                key: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_1_key.pem')}",
            ],
            [
                ca: "$etcd_ca",
                cert: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_2_cert.pem')}",
                key: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_2_key.pem')}",
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
        joinCommand: "kubeadm join 185.24.220.41:6443 --token 596k28.2yk8y09e86h3vpcu --discovery-token-ca-cert-hash sha256:9b6152934be7e2bf7c0214680823276b16712ebb61b6e2d874760221ae2b0106",
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
                cert: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_cert.pem')}",
                key: "${AndreaClusterResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_key.pem')}",
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
        hosts: [
            "sso.andrea.muellerpublic.de"
        ]
    ]

    static final mariadbVars = [
        image: [name: "bitnami/mariadb", version: "10.1.38-debian-9-r65"],
        admin: [user: "root", password: "wooSh6ohrah2AiCiY7uchoh5Leisee4x"],
        host: "robobeerun-com-mariadb.robobeerun-com-mariadb.svc",
        port: 3306
    ]
	
	static final emailVars = [
		host: "andrea-mail-0.muellerpublic.de:465",
	]

}
