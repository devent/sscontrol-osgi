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
package com.anrisoftware.sscontrol.muellerpublicde.c_basics

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.cluster_test.ClusterTestResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.cluster_test.ClusterTestMastersNodesSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
class C_01_KeycloakTest extends Abstract_Runner_Debian_Test {

    @Test
    void "keycloak"() {
        def test = [
            name: "keycloak",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/keycloak-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/v5.0.0-r.1"
}
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        db: [
            image: postgresVars.image,
            admin: postgresVars.admin,
            host: postgresVars.host,
            port: postgresVars.port,
            database: "keycloakdb", user: "keycloakdb", password: "pie1zuroiXa5Aish6caeT8gaex6uthi2",
        ]
    ]
    vars << [
        keycloak: [
            image: [name: "jboss/keycloak", version: "5.0.0"],
            limits: [cpu: "500m", memory: "600Mi"],
            requests: [cpu: "500m", memory: "600Mi"],
            issuer: "selfsigning-issuer",
            revision: revision,
            java: [heapStart: "200m", heapMax: "200m", metaspaceStart: "200m", metaspaceMax: "200m"],
            adminUser: "admin",
            adminPassword: "admin1234",
            affinity: [key: "robobeerun.com/keycloak", name: "required", required: true],
            allowOnMaster: true,
            hosts: keycloakVars.hosts,
        ]
    ]
    vars << [
        pgbouncer: [
            image: pgbouncerVars.image,
            limits: [cpu: "100m", memory: "100Mi"],
            requests: [cpu: '100m', memory: '100Mi'],
        ]
    ]
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                postgresVars: postgresVars, revision: "r1", keycloakVars: keycloakVars, pgbouncerVars: pgbouncerVars],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
