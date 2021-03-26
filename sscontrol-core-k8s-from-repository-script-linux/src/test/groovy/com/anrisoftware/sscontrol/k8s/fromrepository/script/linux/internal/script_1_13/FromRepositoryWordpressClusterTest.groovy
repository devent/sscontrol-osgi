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
class FromRepositoryWordpressClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "wordpress"() {
        def test = [
            name: "wordpress",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "wordpress" with {
    remote url: "git@github.com:robobee-repos/muellerpublic-de-wordpress-deploy.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/r2"
}
service "from-repository", repo: "wordpress" with {
    vars << [
        volume: [
            storage: "10Gi"
        ]
    ]
    vars << [
        db: [
            root: [user: "root", password: "ba6aikahXi7ya0moothieLohb9eesi6u"],
            wordpress: [database: "wordpressdb", user: "wordpress", password: "wordpress"],
        ]
    ]
    vars << [
        mariadb: [
            image: [name: "bitnami/mariadb", version: "10.1.38"],
            host: "mariadb.robobeerun-com-mariadb.svc",
            port: "3306",
        ]
    ]
    vars << [
        wordpress: [
            image: [name: "erwin82/muellerpublic-de-wordpress", version: "v5.1.1-php7.1-fpm-r.1"],
            limits: [cpu: "100m", memory: "300Mi"],
            issuer: "selfsigning-issuer",
            php: [
                memoryLimit: "200M",
                maxExecutionTime: 300,
                maxChildren: 4,
                startServers: 1,
                minSpareServers: 1,
                maxSpareServers: 1,
                maxRequests: 10000,
                slowlogTimeout: 0,
                catchWorkersOutput: 1,
                opcacheEnable: 1,
                opcacheEnableCLI: 1,
                opcacheMemoryConsumption: "24M"
            ],
        ]
    ]
    vars << [
        nginx: [
            image: [name: "robobeerun/nginx", version: "v1.13.12-r1"],
            limits: [cpu: "100m", memory: "100Mi"],
            hosts: ["www.muellerpublic.de.robobee.test"],
            httpHeaders: [[name: "Host", value: "www.muellerpublic.de.robobee.test"]],
            revision: "r3",
            workerProcesses: "1",
            workerConnections: "512",
            clientMaxBodySize: "8M"
        ]
    ]
    vars << [
        rsync: [
            image: [name: "robobeerun/rsync", version: "v3.1.2-r2"],
            limits: [cpu: "20m", memory: "20Mi"],
            ssh: [
                revision: "r1",
                publicKey: robobeePub,
            ],
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
