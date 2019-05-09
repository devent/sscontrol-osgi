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
package com.anrisoftware.sscontrol.muellerpublicde.b_k8s

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterMastersNodesSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(AndreaClusterMastersNodesSocketCondition.class)
class B_05_CertManagerTest extends Abstract_Runner_Debian_Test {

    @Test
    void "cert-manager"() {
        def test = [
            name: "cert-manager",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "cert-manager" with {
    remote url: "git@github.com:robobee-repos/cert-manager.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/v0.7.0-k8s1.13-r.1"
}
service "from-repository", repo: "cert-manager" with {
    vars << [
        clusterIssuer: [
            selfsigning: [enabled: true],
            letsencrypt: [
                prod: [enabled: true],
                staging: [enabled: true],
                email: "admin@muellerpublic.de",
            ]
        ]
    ]
    vars << [
        certManager: [
            limits: [cpu: '0.1', memory: '50Mi'],
            requests: [cpu: '0.1', memory: '50Mi'],
            affinity: [key: "robobeerun.com/cert-manager", name: "required", required: true],
        ]
    ]
    vars << [
        certManagerCainjector: [
            limits: [cpu: '0.1', memory: '50Mi'],
            requests: [cpu: '0.1', memory: '50Mi'],
        ]
    ]
    vars << [
        certManagerWebhook: [
            limits: [cpu: '0.1', memory: '50Mi'],
            requests: [cpu: '0.1', memory: '50Mi'],
        ]
    ]
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
