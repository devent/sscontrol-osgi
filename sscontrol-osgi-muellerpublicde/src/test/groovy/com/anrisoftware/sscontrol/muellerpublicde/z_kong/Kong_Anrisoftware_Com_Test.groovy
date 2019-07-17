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
package com.anrisoftware.sscontrol.muellerpublicde.z_kong

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
class Kong_Anrisoftware_Com_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "kong-anrisoftware-com"() {
        def test = [
            name: "kong-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "kong-anrisoftware-com" with {
    remote url: "git@anrisoftware.com:devent/kong-anrisoftware-com-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "develop"
}
service "from-repository", repo: "kong-anrisoftware-com", dryrun: false with {
    vars << [
        db: [
            image: postgresVars.image,
            admin: postgresVars.admin,
            host: postgresVars.host,
            port: postgresVars.port,
            database: "kong15", user: "kong", password: "ien3etheReechaitoepeeFi7Eepo3udo",
            schema: "public",
            revision: "r1",
        ]
    ]
    vars << [
        kong: [
            image: [name: 'kong', version: '0.15-alpine'],
			revision: "r1",
            hosts: [
                admin: [name: "admin.kong.anrisoftware.com", port: 8001],
                adminGui: [name: "gui.kong.anrisoftware.com", port: 8002],
                devPortal: [name: "portal.kong.anrisoftware.com", port: 8003],
                proxy: [name: "kong.anrisoftware.com", port: 8000],
            ],
            issuer: "letsencrypt-prod",
            livenessProbe: [port: "admin"],
            readinessProbe: [port: "admin"],
        ]
    ]
    vars << [
        kongDashboard: [
            image: [name: 'pgbi/kong-dashboard', version: 'v3'],
            livenessProbe: [port: "admin-gui"],
            readinessProbe: [port: "admin-gui"],
        ]
    ]
    vars << [
        pgbouncer: [
            image: pgbouncerVars.image,
            limits: [cpu: "0.1", memory: "100Mi"],
            requests: [cpu: '0.1', memory: '100Mi'],
        ]
    ]
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                postgresVars: postgresVars,
				pgbouncerVars: pgbouncerVars,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
