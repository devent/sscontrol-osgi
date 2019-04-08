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
class D_01_JavadocAnrisoftwareTest extends Abstract_Runner_Debian_Test {

    static final jenkinsPublicKey = D_01_JavadocAnrisoftwareTest.class.getResource("jenkins_id_rsa_pub.txt")

    @Test
    void "javadoc-anrisoftware-com"() {
        def test = [
            name: "javadoc-anrisoftware-com",
            script: '''
def affinityKey = "muellerpublic.de/javadoc-anrisoftware-com"
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/javadoc-anrisoftware-com-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "develop"
}
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        javadoc: [
            ssh: [revision: "r1", publicKey: jenkins.publicKey],
            nginx: [revision: "r1"],
        ],
    ]
    vars << [
        volume: [class: "managed-nfs-storage", storage: "10Gi"],
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.12-r1'],
            limits: [cpu: '50m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '100Mi'],
            affinity: [key: affinityKey, name: "required", required: true],
            workerProcesses: 2,
            workerConnections: 4096,
            readTimeout: 300,
            clientMaxBodySize: "64m",
            hosts: [
                'javadoc.anrisoftware.com', // main domain
            ],
        ],
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r3'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            affinity: [key: affinityKey, name: "required", required: true],
        ]
    ]
}
''',
            scriptVars: [
                socketFiles: socketFiles,
                k8sVars: k8s_vars,
                robobeeKey: robobeeKey,
                jenkins: [publicKey: jenkinsPublicKey]
            ],
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
