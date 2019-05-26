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
package com.anrisoftware.sscontrol.muellerpublicde.z_www_interscalar_com

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
class Www_Interscalar_ComTest extends Abstract_Runner_Debian_Test {

    @Test
    void "interscalar-com-wordpress"() {
        def test = [
            name: "interscalar-com-wordpress",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "interscalar-com-wordpress" with {
    remote url: "git@github.com:robobee-repos/interscalar-com-wordpress.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/r3"
}
service "from-repository", repo: "interscalar-com-wordpress", dryrun: false with {
    vars << [
        volume: [storage: "5Gi"]
    ]
    vars << [
        db: [
            image: mariadbVars.image,
            admin: mariadbVars.admin,
            host: mariadbVars.host,
            port: mariadbVars.port,
            database: 'interscalardb',
            user: 'interscalardb',
            password: "Ievoo0ohhah6oShooneekaeF2aibevae",
            revision: revision
        ]
    ]
    vars << [
        wordpress: [
            image: [name: 'robobeerun/wordpress', version: 'v5.2.0-php7.1-fpm-r.2'],
            limits: [cpu: '500m', memory: '500Mi'],
            requests: [cpu: '500m', memory: '500Mi'],
            affinity: [key: "www.interscalar.com", name: "required", required: true],
			revision: revision,
            php: [
                memoryLimit: "200M",
                maxExecutionTime: 600,
                maxChildren: 2,
                startServers: 1,
                minSpareServers: 1,
                maxSpareServers: 1,
				maxRequests: 500,
				slowlogTimeout: 0,
				catchWorkersOutput: 1,
                opcacheEnable: 1,
                opcacheEnableCLI: 1,
                opcacheMemoryConsumption: "64"
            ],
            hosts: [
                'www.interscalar.com', // main domain
                'interscalar.org', 'www.interscalar.org',
                'interscalar.info', 'www.interscalar.info',
                'interscalar.space', 'www.interscalar.space',
                'interscalar.com',
            ],
            issuer: "letsencrypt-prod",
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.12-r1'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '100m', memory: '100Mi'],
			revision: revision,
            readinessProbeEnabled: false,
            httpHeaders: [[name: "Host", value: "www.interscalar.com"]],
            workerProcesses: 4,
            workerConnections: 4096,
            clientMaxBodySize: '64m',
			readTimeout: 600,
        ]
    ]
    vars << [
        exporter: [
            limits: [cpu: '50m', memory: '20Mi'],
            requests: [cpu: '50m', memory: '20Mi'],
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
                mariadbVars: mariadbVars, revision: "r1"
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
