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
class FromRepositoryCertManagerClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "cert-manager to cluster"() {
        def test = [
            name: "cluster_cert_manager",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "cert-manager" with {
    remote url: "git@github.com:robobee-repos/cert-manager.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/v0.7.0-k8s1.13-r.1"
}
service "from-repository", repo: "cert-manager" with {
    vars << [
        clusterIssuer: [selfsigning: [enabled: true ]]
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
            scriptVars: [robobeeSocket: robobeeSocket, robobeeKey: robobeeKey],
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
