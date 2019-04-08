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
class D_01_SonarQubeAnrisoftwareTest extends Abstract_Runner_Debian_Test {

    @Test
    void "sonarqube-anrisoftware-com"() {
        def test = [
            name: "sonarqube-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/anrisoftware-com-sonarqube-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "develop"
}
def affinity = [key: "muellerpublic.de/sonarqube-anrisoftware-com", name: "required", required: true]
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        volumes: [data: [class: "managed-nfs-storage", storage: "20Gi"]],
    ]
    vars << [
        sonarqube: [
            image: [name: 'sonarqube', version: '7.1'],
            limits: [cpu: '250m', memory: '2000Mi'],
            requests: [cpu: '250m', memory: '2000Mi'],
            affinity: affinity,
            port: 8080,
            initialDelaySeconds: "300",
            revision: "r2",
            java: [opts: ["-Xmx512m", "-Xms512m", "-Dsonar.core.serverBaseURL=https://sonarqube.anrisoftware.com"]],
            db: [
                user: "soanrqube",
                password: "kaoX3iesae4ua0ezohdimacohquievi2",
                database: "sonarqubedb",
                jdbc: "jdbc:postgresql://localhost/sonarqubedb"
            ],
            hosts: [
                'sonarqube.anrisoftware.com', // main domain
            ],
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.12-r1'],
            limits: [cpu: '50m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '100Mi'],
            revision: "r2",
            workerProcesses: 2,
            workerConnections: 4096,
            readTimeout: 60,
            clientMaxBodySize: "64m",
        ],
    ]
    vars << [
        postgres: [
            image: [name: 'postgres', version: '10.4'],
            limits: [cpu: '200m', memory: '250Mi'],
            requests: [cpu: '200m', memory: '250Mi'],
            affinity: affinity,
            revision: "r1",
            user: "postgres",
            password: "vaaCoo9AeJooy8Owei4ooso8fakitahx",
            database: "testdb",
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
            affinity: affinity,
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
