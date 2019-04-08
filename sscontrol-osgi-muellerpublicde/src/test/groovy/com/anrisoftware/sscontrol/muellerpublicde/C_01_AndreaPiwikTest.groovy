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
class C_01_AndreaPiwikTest extends Abstract_Runner_Debian_Test {

    @Test
    void "piwik-andrea"() {
        def test = [
            name: "piwik-andrea",
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
service "repo-git", group: "piwik-andrea-muellerpublic-de" with {
    remote url: "git@github.com:robobee-repos/piwik-andrea-muellerpublic-de.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "piwik-andrea-muellerpublic-de" with {
    vars << [
        volume: [storage: "10Gi"]
    ]
    vars << [
        db: [
            root: [user: 'root', password: 'sum0cee3iishi5ahcae2Yahfaiy9zoev'],
            piwik: [db: 'piwikdb', user: 'piwikdb', password: 'te8Laitei5eoJiiceed0aebua7Choo1a'],
            limits: [cpu: '0', memory: '200Mi'],
            requests: [cpu: '0', memory: '0'],
        ]
    ]
    vars << [
        redis: [
            image: [name: 'erwin82/redis-unpriv', version: '3.2-r1-1'],
            limits: [cpu: '0', memory: '50Mi'],
            requests: [cpu: '0', memory: '0'],
        ]
    ]
    vars << [
        piwik: [
            image: [name: 'robobeerun/piwik', version: 'v3.2.0-fpm-r3'],
            limits: [cpu: '0', memory: '500Mi'],
            requests: [cpu: '0', memory: '500Mi'],
            nginx: [
                hosts: ['piwik.andrea.muellerpublic.de'],
            ],
            php: [
                memoryLimit: "100M",
                maxChildren: 100,
                startServers: 10,
                minSpareServers: 10,
                maxSpareServers: 20,
                opcacheEnable: 1,
                opcacheEnableCLI: 1,
                opcacheMemoryConsumption: 64,
                slowlogTimeout: '0'
            ],
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.6-r3'],
            limits: [cpu: '0', memory: '100Mi'],
            requests: [cpu: '0', memory: '0'],
            workerProcesses: 2,
            workerConnections: 4096,
            clientMaxBodySize: '4m',
            readTimeout: 600,
        ]
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r1'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '0', memory: '0'],
            publicKey: k8s_vars.rsync.publicKey,
        ]
    ]
}
''',
            scriptVars: [socketFiles: socketFiles, k8s_vars: k8s_vars, robobeeKey: robobeeKey],
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
