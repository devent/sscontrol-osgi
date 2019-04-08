/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
class D_01_AnrisoftwareTest extends Abstract_Runner_Debian_Test {

    @Test
    void "anrisoftware-com"() {
        def test = [
            name: "anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/anrisoftware-com.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/r2"
}
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        volume: [class: "managed-nfs-storage", storage: "20Gi"],
    ]
    vars << [
        postgres: [
            image: [name: 'postgres', version: '10.4'],
            limits: [cpu: '200m', memory: '350Mi'],
            requests: [cpu: '200m', memory: '350Mi'],
            affinity: [key: "muellerpublic.de/anrisoftware-com", name: "required", required: true],
            revision: "r3",
            user: "postgres",
            password: "ahthi1ju3raefiChithiinga3aiv3koe",
            database: "testdb",
        ],
    ]
    vars << [
        gitea: [
            image: [name: 'robobeerun/gitea', version: 'v1.5.0-r.1'],
            limits: [cpu: '200m', memory: '500Mi'],
            requests: [cpu: '200m', memory: '500Mi'],
            affinity: [key: "muellerpublic.de/anrisoftware-com", name: "required", required: true],
            initialDelaySeconds: "60",
            httpHeaders: [[name: "Host", value: "gitea.anrisoftware.com"]],
            revision: "r4",
            postgres: [
                user: "giteadb",
                password: "eeshoob1ieyeeshei4za9Yo6sohceal3",
                database: "giteadb",
            ],
            hosts: [
                'gitea.anrisoftware.com', // main domain
            ],
        ],
    ]
    vars << [
        redmine: [
            image: [name: 'erwin82/redmine', version: 'v3.4.6-puma-r2'],
            limits: [cpu: '500m', memory: '1000Mi'],
            requests: [cpu: '500m', memory: '1000Mi'],
            affinity: [key: "muellerpublic.de/anrisoftware-com", name: "required", required: true],
            initialDelaySeconds: "100",
            httpHeaders: [[name: "Host", value: "project.anrisoftware.com"]],
            revision: "r3",
            puma: [
                minThreads: 8,
                maxThreads: 16,
                workers: 2,
                timeout: 300,
            ],
            postgres: [
                adminPassword: "yae2eij0mahyeekiochoebaishieCohZ",
                user: "anrisprojects",
                password: "WohCh4co0waorees9nechaezuagoz4ai",
                database: "anrisprojects",
                schema: "anrisprojects",
            ],
            hosts: [
                'project.anrisoftware.com', // main domain
                'anrisoftware.com',
            ],
        ]
    ]
    vars << [
        archiva: [
            image: [name: 'robobeerun/archiva', version: 'v2.2.3-r2'],
            limits: [cpu: '250m', memory: '2000Mi'],
            requests: [cpu: '250m', memory: '2000Mi'],
            affinity: [key: "muellerpublic.de/anrisoftware-com", name: "required", required: true],
            initialDelaySeconds: "300",
            httpHeaders: [[name: "Host", value: "maven.anrisoftware.com"]],
            revision: "r3",
            java: [
                initialMemory: "1500m",
                maxMemory: "1500m",
            ],
            hosts: [
                'maven.anrisoftware.com', // main domain
            ],
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.12-r1'],
            limits: [cpu: '50m', memory: '100Mi'],
            requests: [cpu: '10m', memory: '50Mi'],
            workerProcesses: 2,
            workerConnections: 4096,
            readTimeout: 300,
            clientMaxBodySize: "64m",
        ],
    ]
    vars << [
        pgbouncer: [
            image: [name: 'crunchydata/crunchy-pgbouncer', version: 'centos7-10.4-1.8.3'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '100Mi'],
        ],
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r2'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            affinity: [key: "muellerpublic.de/anrisoftware-com", name: "required", required: true],
            ssh: [revision: "r3", publicKey: k8sVars.rsync.publicKey],
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
