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
package com.anrisoftware.sscontrol.muellerpublicde.cloud_muellerpublic_de

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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
class Cloud_Muellerpublic_De_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "cloud-muellerpublic-de"() {
        def test = [
            name: "cloud-muellerpublic-de",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "cloud-muellerpublic-de-nextcloud-deploy" with {
    remote url: "git@github.com:robobee-repos/cloud-muellerpublic-de-nextcloud-deploy.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/r1"
}
service "from-repository", repo: "cloud-muellerpublic-de-nextcloud-deploy" with {
    vars << [
        volume: [name: "cloud-muellerpublic-de-nextcloud-0", storage: "10Gi"]
    ]
    vars << [
        db: [
            image: mariadbVars.image,
            admin: mariadbVars.admin,
            host: mariadbVars.host,
            port: mariadbVars.port,
            database: 'nextclouddb',
            user: 'nextclouddb',
            password: "ushothiengao5aeghieph2Uoke5fufil",
            revision: "r1"
        ]
    ]
    vars << [
        nextcloud: [
            image: [name: 'robobeerun/nextcloud', version: 'v16.0.1-fpm-r.1'],
            limits: [cpu: '500m', memory: '1500Mi'],
            requests: [cpu: '500m', memory: '1500Mi'],
            cron: [schedule: "0 */4 * * *"],
            affinity: [key: "cloud.muellerpublic.de", name: "required", required: true],
            revision: "r3",
            hosts: [
                'cloud.muellerpublic.de', // main domain
            ],
            issuer: "letsencrypt-prod",
            php: [
                memoryLimit: "512M",
                maxExecutionTime: 600,
                maxChildren: 4,
                startServers: 2,
                minSpareServers: 2,
                maxSpareServers: 4,
                maxRequests: 1000,
                slowlogTimeout: 0,
                catchWorkersOutput: 1,
                opcacheEnable: 1,
                opcacheEnableCLI: 1,
                opcacheMemoryConsumption: "128"
            ],
            email: [
                host: emailVars.host,
                port: emailVars.port,
                user: "cloud@muellerpublic.de",
                password: "roh7buko7eiYar3w",
                secure: "ssl",
                authType: "LOGIN",
                from: "admin@muellerpublic.de",
                domain: "muellerpublic.de"
            ],
        ]
    ]
    vars << [
        redis: [
            image: [name: 'redis', version: '5.0.5-stretch'],
            limits: [cpu: '100m', memory: '50Mi'],
            requests: [cpu: '100m', memory: '50Mi'],
            affinity: [key: "cloud.muellerpublic.de", name: "required", required: true],
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.15.12-r.1'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '100m', memory: '100Mi'],
            revision: "r2",
            readinessProbeEnabled: true,
            httpHeaders: [[name: "Host", value: "cloud.muellerpublic.de"]],
            workerProcesses: 4,
            workerConnections: 4096,
            clientMaxBodySize: '64m',
            readTimeout: 600,
        ],
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
                mariadbVars: mariadbVars,
                emailVars: emailVars,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
