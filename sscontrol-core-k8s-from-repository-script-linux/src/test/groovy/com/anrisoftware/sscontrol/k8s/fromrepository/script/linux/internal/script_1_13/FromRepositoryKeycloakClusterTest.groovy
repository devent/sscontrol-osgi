/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(RobobeeSocketCondition.class)
class FromRepositoryKeycloakClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "keycloak"() {
        def test = [
            name: "keycloak",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "keycloak" with {
    remote url: "git@github.com:robobee-repos/keycloak-deploy.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/v5.0.0-r.1"
}
def revision = "r2"
service "from-repository", repo: "keycloak" with {
    vars << [
        db: [
            image: [name: "bitnami/postgresql", version: "10.7.0-debian-9-r48"],
            database: "wordpressdb", user: "wordpressdb", password: "wordpressdb",
            admin: [user: "postgres", password: "oophiReighiela6choo1eiWainguliej"],
            host: "postgres-postgresql.robobeerun-com-postgres.svc",
            port: 5432,
        ]
    ]
    vars << [
        keycloak: [
            image: [name: "jboss/keycloak", version: "5.0.0"],
            limits: [cpu: "500m", memory: "600Mi"],
            issuer: "selfsigning-issuer",
            revision: revision,
            java: [heapStart: "200m", heapMax: "200m", metaspaceStart: "200m", metaspaceMax: "200m"],
            adminUser: "admin",
            adminPassword: "admin1234",
            affinity: [key: "robobeerun.com/keycloak", name: "required", required: true],
            allowOnMaster: true,
            hosts: [
                "keycloak.robobee.test", // main domain
            ],
        ]
    ]
    vars << [
        pgbouncer: [
            image: [name: "crunchydata/crunchy-pgbouncer", version: "centos7-10.7-2.3.1"],
            limits: [cpu: "100m", memory: "100Mi"],
            requests: [cpu: '100m', memory: '100Mi'],
        ]
    ]
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
                robobeePub: robobeePub,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
