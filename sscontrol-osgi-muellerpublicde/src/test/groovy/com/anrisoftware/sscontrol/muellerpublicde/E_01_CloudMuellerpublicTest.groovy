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
class E_01_CloudMuellerpublicTest extends Abstract_Runner_Debian_Test {

    @Test
    void "cloud-muellerpublic-de"() {
        def test = [
            name: "cloud-muellerpublic-de",
            script: '''
service "ssh" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "masters" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "nodes" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.nodes[0]
    host "robobee@andrea-node-1.muellerpublic.de", socket: socketFiles.nodes[1]
}
service "k8s-cluster", target: 'masters' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}
service "repo-git", group: "cloud-muellerpublic-de" with {
    remote url: "git@github.com:robobee-repos/cloud-muellerpublic-de.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "cloud-muellerpublic-de" with {
    vars << [
        volume: [storage: "10Gi"]
    ]
    vars << [
        postgres: [
            image: [name: 'centos/postgresql-96-centos7', version: '9.6'],
            limits: [cpu: '0', memory: '200Mi'],
            requests: [cpu: '0', memory: '200Mi'],
            adminPassword: "yae2eij0mahyeekiochoebaishieCohZ",
            user: "testdb",
            password: "ahthi1ju3raefiChithiinga3aiv3koe",
            database: "testdb",
            affinityRequired: true,
        ],
    ]
    vars << [
        cloud: [
            image: [name: 'erwin82/cloud-muellerpublic-de', version: 'nextcloud-v12.0.4-fpm-r1'],
            limits: [cpu: '0', memory: '500Mi'],
            requests: [cpu: '0', memory: '500Mi'],
            cron: [schedule: "0 4 * * *"],
            affinityRequired: true,
            db: [
                user: "nextclouddb",
                password: "eeshoob1ieyeeshei4za9Yo6sohceal3",
                database: "nextclouddb",
                adminPassword: "yae2eij0mahyeekiochoebaishieCohZ",
            ],
            nginx: [
                hosts: [
                    'cloud.muellerpublic.de', // main domain
                ],
            ],
            php: [
                memoryLimit: '100M',
                maxChildren: 20,
                startServers: 10,
                minSpareServers: 10,
                maxSpareServers: 15,
                opcacheEnable: 1,
                opcacheEnableCLI: 1,
                opcacheMemoryConsumption: 64,
                slowlogTimeout: '0'
            ],
        ]
    ]
    vars << [
        redis: [
            image: [name: 'robobeerun/redis', version: 'v4.0.4-alpine-r1'],
            limits: [cpu: '0', memory: '50Mi'],
            requests: [cpu: '0', memory: '0'],
            affinityRequired: true,
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.6-r3'],
            limits: [cpu: '0', memory: '100Mi'],
            requests: [cpu: '0', memory: '0'],
            workerProcesses: 2,
            workerConnections: 4096,
            readTimeout: 600,
            clientMaxBodySize: "64m",
            affinityRequired: true,
        ],
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r1'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '0', memory: '0'],
            publicKey: k8s_vars.rsync.publicKey,
            affinityRequired: true,
        ]
    ]
}
''',
            scriptVars: [
                socketFiles: socketFiles,
                k8s_vars: k8s_vars,
                robobeeKey: robobeeKey
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Test
    void "backup-cloud-muellerpublic-de"() {
        def test = [
            name: "backup-cloud-muellerpublic-de",
            script: '''
service "ssh" with {
    host "robobee@localhost", socket: localhostSocket
}
service "ssh", group: "master" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster", target: 'master' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "backup" with {
    service namespace: "cloud-muellerpublic-de", name: "postgres"
    destination arguments: "-c --delete", dir: "${dailyPath}/cloud-muellerpublic-de/postgres"
    client key: k8s_vars.rsync.key, proxy: true, timeout: "1h"
}
service "backup" with {
    service namespace: "cloud-muellerpublic-de", name: "cloud"
    destination arguments: "-c --delete", dir: "${dailyPath}/cloud-muellerpublic-de/cloud"
    client key: k8s_vars.rsync.key, proxy: true, timeout: "4h"
    source << "/html"
    source << "/data"
}
''',
            scriptVars: [
                localhostSocket: localhostSocket,
                socketFiles: socketFiles,
                k8s_vars: k8s_vars
            ],
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Test
    void "restore-cloud-muellerpublic-de"() {
        assumeSocketExists localhostSocket
        def test = [
            name: "restore-cloud-muellerpublic-de",
            script: '''
service "ssh" with {
    host "robobee@localhost", socket: localhostSocket
}
service "ssh", group: "masters" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster", target: 'masters' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "restore" with {
    service namespace: "cloud-muellerpublic-de", name: "postgres"
    origin arguments: "-c --delete", dir: "${dailyPath}/cloud-muellerpublic-de/postgres"
    client key: k8s_vars.rsync.key, proxy: true, timeout: "4h"
    source << [target: "/data", chown: "26.26", chmod: "u=rwX-s,g=-s,o=-s"]
}
//service "restore" with {
//    service namespace: "cloud-muellerpublic-de", name: "redmine-app"
//    origin arguments: "-c --delete", dir: "${dailyPath}/cloud-muellerpublic-de/redmine-app"
//    client key: k8s_vars.rsync.key, proxy: true, timeout: "2h"
//    source << [target: "/data", chown: "33.2006", chmod: "u=rwX-s,g=rwX-s,o-s"]
//    source << [target: "/html", chown: "33.2006", chmod: "u=rwX-s,g=rwX-s,o-s"]
//}
''',
            scriptVars: [
                localhostSocket: localhostSocket,
                socketFiles: socketFiles,
                k8s_vars: k8s_vars
            ],
            expectedServicesSize: 3,
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
