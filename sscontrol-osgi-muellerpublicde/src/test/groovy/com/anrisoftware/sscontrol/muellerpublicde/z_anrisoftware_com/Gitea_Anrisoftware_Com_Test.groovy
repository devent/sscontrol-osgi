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
package com.anrisoftware.sscontrol.muellerpublicde.z_anrisoftware_com

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterMastersOnlySocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(AndreaClusterMastersOnlySocketCondition.class)
class Gitea_Anrisoftware_Com_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "gitea-anrisoftware-com"() {
        def test = [
            name: "gitea-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "gitea-anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/gitea-anrisoftware-com.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "master"
}
service "from-repository", repo: "gitea-anrisoftware-com", dryrun: false with {
    vars << [
        volume: [storage: "10Gi"]
    ]
    vars << [
        db: [
            image: postgresVars.image,
            admin: postgresVars.admin,
            host: postgresVars.host,
            port: postgresVars.port,
            database: "giteadb", user: "giteadb", password: "guk2eePua1eeVatior5Quohf7mu5ash6",
			schema: "public",
			revision: "r1",
            type: "postgres",
        ]
    ]
    vars << [
        gitea: [
            image: [name: 'robobeerun/gitea', version: 'v1.8.1-r.1'],
            limits: [cpu: '250m', memory: '250Mi'],
            requests: [cpu: '250m', memory: '250Mi'],
            affinity: [key: "gitea.anrisoftware.com", name: "required", required: true],
			livenessProbe: [port: "http", initialDelaySeconds: 100],
			readinessProbe: [port: "http", initialDelaySeconds: 100],
            httpHeaders: [[name: "Host", value: "gitea.anrisoftware.com"]],
            revision: "r7",
            hosts: [
                'gitea.anrisoftware.com', // main domain
            ],
			issuer: "letsencrypt-prod",
            appName: "Anrisoftware Projects",
            runMode: "prod",
            googleAnalyticsTag: "UA-119767261-1",
			secretKey: "eijaigiet0eel3mi5foh9ieJ5aawi7sh",
			email: [host: emailVars.host, from: "git@anrisoftware.com", user: "git@anrisoftware.com", password: "oPheezahKo2quur8"],
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.15.12-r.1'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '100m', memory: '100Mi'],
			revision: "r3",
            workerProcesses: 4,
            workerConnections: 4096,
            clientMaxBodySize: '64m',
			readTimeout: 600,
        ]
    ]
    vars << [
        pgbouncer: [
            image: pgbouncerVars.image,
            limits: [cpu: "0.1", memory: "100Mi"],
            requests: [cpu: '0.1', memory: '100Mi'],
        ]
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r2'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            ssh: [revision: "r1", publicKey: k8sVars.rsync.publicKey],
        ]
    ]
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                postgresVars: postgresVars,
				pgbouncerVars: pgbouncerVars,
				emailVars: emailVars,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
