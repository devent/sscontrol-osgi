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
class C_01_GraphiteTest extends Abstract_Runner_Debian_Test {

    @Test
    void "graphite-andrea"() {
        def test = [
            name: "graphite-andrea",
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
service "repo-git", group: "graphite-andrea-muellerpublic-de" with {
    remote url: "git@github.com:robobee-repos/graphite-andrea-muellerpublic-de.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "graphite-andrea-muellerpublic-de" with {
    vars << [
        volume: [ storage: "10Gi" ]
    ]
    vars << [
        graphite: [
            image: [name: 'erwin82/graphite-statsd', version: 'v1.0.2-r1-1'],
            limits: [cpu: '0', memory: '400Mi'],
            requests: [cpu: '0', memory: '400Mi'],
            allowOnMaster: true,
            affinityRequired: true,
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'erwin82/nginx-base', version: '1.13.1-r3'],
            limits: [cpu: '0', memory: '100Mi'],
            requests: [cpu: '0', memory: '50Mi'],
            hosts: ['graphite.andrea.muellerpublic.de'],
            workerProcesses: 2,
            workerConnections: 4096,
            clientMaxBodySize: '4m',
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
