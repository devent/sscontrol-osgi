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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FromRepositoryWordpressClusterTest extends AbstractFromRepositoryRunnerTest {

    static final Map certs = [
        worker: [
            ca: FromRepositoryManifestsServerTest.class.getResource('robobee_test_kube_ca.pem'),
            cert: FromRepositoryManifestsServerTest.class.getResource('robobee_test_kube_admin_cert.pem'),
            key: FromRepositoryManifestsServerTest.class.getResource('robobee_test_kube_admin_key.pem'),
        ],
    ]

    @Test
    void "wordpress"() {
        def test = [
            name: "wordpress",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', ca: certs.worker.ca, cert: certs.worker.cert, key: certs.worker.key
}
service "repo-git", group: "wordpress" with {
    remote url: "git@github.com:robobee-repos/interscalar-com-wordpress.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "wordpress" with {
    vars << [
        volume: [
            storage: "1Gi"
        ]
    ]
    vars << [
        db: [
            image: [name: "bitnami/mariadb", version: "10.2.14-r13"],
            limits: [cpu: "100m", memory: "100Mi"],
            affinity: [key: "muellerpublic.de/web", name: "interscalar", required: true],
            root: [user: "mysql", password: "1234"],
            wordpress: [database: "wordpressdb", user: "wordpress", password: "wordpress"],
        ]
    ]
    vars << [
        nginx: [
            image: [name: "robobeerun/nginx", version: "v1.13.12-r1"],
            limits: [cpu: "100m", memory: "100Mi"],
            hosts: ["www.interscalar.test"],
            revision: "r4",
            workerProcesses: "128",
            workerConnections: "1",
            clientMaxBodySize: "8M"
        ]
    ]
    vars << [
        wordpress: [
            image: [name: "erwin82/interscalar-com-wordpress", version: "v5-r1"],
            limits: [cpu: "100m", memory: "200Mi"],
            php: [
                memoryLimit: "32M",
                maxExecutionTime: 300,
                maxChildren: 4,
                startServers: 1,
                minSpareServers: 1,
                maxSpareServers: 1,
                slowlogTimeout: 0,
                catchWorkersOutput: 1,
                opcacheEnable: 1,
                opcacheEnableCLI: 1,
                opcacheMemoryConsumption: "12M"
            ],
        ]
    ]
    vars << [
        rsync: [
            image: [name: "robobeerun/rsync", version: "v3.1.2-r2"],
            limits: [cpu: "20m", memory: "20Mi"],
            publicKey: robobeePub,
        ]
    ]
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
                robobeePub: robobeePub,
                certs: certs,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
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
