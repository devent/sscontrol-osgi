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
package com.anrisoftware.sscontrol.muellerpublicde

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class C_02_KeycloakTest extends Abstract_Runner_Debian_Test {

    @Test
    void "keycloak"() {
        def test = [
            name: "keycloak",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/keycloak-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/r2"
}
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        volume: [class: "managed-nfs-storage", storage: "1Gi"],
    ]
    vars << [
        postgres: [
            image: [name: 'postgres', version: '10.4'],
            limits: [cpu: '100m', memory: '150Mi'],
            requests: [cpu: '100m', memory: '150Mi'],
            affinity: [key: "robobeerun.com/keycloak", name: "required", required: true],
	        allowOnMaster: true,
            revision: "r1",
            user: "postgres",
            password: "chi3ha8jeip1Faexuzikue3za6eibohv",
            database: "testdb",
        ],
    ]
    vars << [
        keycloak: [
            image: [name: 'jboss/keycloak', version: '4.0.0.Final'],
            limits: [cpu: '500m', memory: '600Mi'],
            requests: [cpu: '500m', memory: '600Mi'],
            affinity: [key: "robobeerun.com/keycloak", name: "required", required: true],
	        allowOnMaster: true,
            initialDelaySeconds: "300",
            revision: "r1",
	        java: [heapStart: "200m", heapMax: "200m", metaspaceStart: "200m", metaspaceMax: "200m"],
	        adminUser: "admin",
	        adminPassword: "ooVaiF0thohquaxo",
            postgres: [
                user: "keycloak",
                password: "ochaingaojaeci9Aedahgh3waecher6i",
                database: "keycloakdb",
            ],
            hosts: [
                'sso.andrea.muellerpublic.de', // main domain
            ],
	        nginx: [
	            clientMaxBodySize: "2m"
	        ],
        ],
    ]
    vars << [
        pgbouncer: [
            image: [name: 'crunchydata/crunchy-pgbouncer', version: 'centos7-10.4-1.8.3'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '100m', memory: '100Mi'],
        ],
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r2'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            affinity: [key: "robobeerun.com/keycloak", name: "required", required: true],
	        allowOnMaster: true,
            ssh: [revision: "r1", publicKey: k8sVars.rsync.publicKey],
        ]
    ]
}
''',
            scriptVars: [socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeMastersExists()
        assumeNodesExists()
    }
}
