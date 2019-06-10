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

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import static com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterMastersNodesSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(AndreaClusterMastersNodesSocketCondition.class)
class Javadoc_Anrisoftware_Com_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "javadoc-anrisoftware-com"() {
        def test = [
            name: "javadoc-anrisoftware-com",
            script: '''
def affinityKey = "muellerpublic.de/javadoc-anrisoftware-com"
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/javadoc-anrisoftware-com-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/r1"
}
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        javadoc: [
			revision: "r1",
            ssh: [publicKey: jenkins.publicKey],
			storage: [name: "javadoc-anrisoftware-com-data", size: "10Gi"],
            affinity: [key: "javadoc.anrisoftware.com", name: "required", required: true],
            hosts: [
                'javadoc.anrisoftware.com', // main domain
            ],
			issuer: "letsencrypt-prod",
			indexHtml: """
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Titel</title>
  </head>
  <body>
  </body>
</html>
"""
        ],
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.15.12-r.1'],
            limits: [cpu: '50m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '100Mi'],
            workerProcesses: 2,
            workerConnections: 4096,
            readTimeout: 300,
            clientMaxBodySize: "64m",
        ],
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r3'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
        ]
    ]
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                jenkins: [publicKey: jenkinsPublicKey]
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
