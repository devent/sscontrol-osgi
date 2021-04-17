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

import org.junit.jupiter.api.BeforeEach
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
class FromRepositoryHeapsterGrafanaPersistentClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "heapster_grafana_persistent"() {
        def test = [
            name: "heapster_grafana_persistent",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: sockets.masters[0]
service "k8s-cluster"
service "repo-git", group: "heapster-influxdb-grafana-monitoring" with {
    remote url: "git@github.com:robobee-repos/kube-cluster-monitoring.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "feature/v1.6.0-beta.1-r.0"
}
service "from-repository", repo: "heapster-influxdb-grafana-monitoring", dest: "/etc/kubernetes/addons/cluster-monitoring" with {
    vars << [
        heapster: [
            image: [name: "k8s.gcr.io/heapster-amd64", version: "v1.6.0-beta.1"],
            affinity: [key: "robobeerun.com/heapster", name: "required", required: true],
            allowOnMaster: true,
            minClusterSize: 3
        ],
        eventer: [
            baseCpu: '25m', extraCpu: '0', baseMemory: '40Mi', extraMemory: '5Mi',
        ],
        resizer: [
            image: [name: 'k8s.gcr.io/addon-resizer', version: '1.8.3']
        ],
        nanny: [
            limits: [cpu: '25m', memory: '60Mi'],
            requests: [cpu: '25m', memory: '60Mi'],
        ],
        metrics: [
            baseCpu: '25m', extraCpu: '0', baseMemory: '40Mi', extraMemory: '1Mi',
        ]
    ]
    vars << [
        influxGrafana: [
            image: [version: 'v4'],
            affinity: [key: "robobeerun.com/heapster", name: "required", required: true],
            allowOnMaster: true
        ]
    ]
    vars << [
        influxdb: [
            image: [name: 'k8s.gcr.io/heapster-influxdb-amd64', version: 'v1.3.3'],
            limits: [cpu: '0', memory: '300Mi'],
            requests: [cpu: '0', memory: '300Mi'],
            persistent: [share: true, storage: [class: "managed-nfs-storage", size: "1Gi"]],
            revision: "r1",
        ]
    ]
    vars << [
        grafana: [
            image: [name: 'k8s.gcr.io/heapster-grafana-amd64', version: 'v4.4.3'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            revision: "r1",
            auth: [basic: true, anonymous: false],
            port: 3000,
        ]
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r2'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            affinity: [key: "robobeerun.com/influx", name: "required", required: true],
            ssh: [revision: "r1", publicKey: "xx"],
        ]
    ]
}
''',
            scriptVars: [
                sockets: [
                    masters: [
                        "/tmp/robobee@robobee-test:22"
                    ],
                    nodes: [
                        "/tmp/robobee@robobee-test:22",
                        "/tmp/robobee@robobee-1-test:22"
                    ]
                ],
                robobeeKey: robobeeKey,
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
