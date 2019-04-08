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
class D_01_NexusAnrisoftwareTest extends Abstract_Runner_Debian_Test {

    @Test
    void "nexus-anrisoftware-com"() {
        def test = [
            name: "nexus-anrisoftware-com",
            script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/nexus-anrisoftware-com-deploy"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/r.1"
}
def nexusAffinity = "muellerpublic.de/nexus-anrisoftware-com"
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        volume: [class: "managed-nfs-storage", storage: "20Gi"],
    ]
    vars << [
        nexus: [
            image: [name: 'erwin82/nexus', version: 'v3.13.0-r.3'],
            limits: [cpu: '250m', memory: '2000Mi'],
            requests: [cpu: '250m', memory: '2000Mi'],
            affinity: [key: nexusAffinity, name: "required", required: true],
            port: 8081,
            initialDelaySeconds: "300",
            httpHeaders: [[name: "Host", value: "maven.anrisoftware.com"]],
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
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r2'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            affinity: [key: nexusAffinity, name: "required", required: true],
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
