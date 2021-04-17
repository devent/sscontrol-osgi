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
class FromRepositoryPrometheusClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "prometheus monitoring addon to cluster"() {
        def test = [
            name: "prometheus",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "prometheus" with {
    remote url: "git@github.com:robobee-repos/kube-prometheus.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/v0.29.0-k8s1.13"
}
service "from-repository", repo: "prometheus" with {
    vars << [
        prometheusOperator: [
            image: [name: "quay.io/coreos/prometheus-operator", version: "v0.29.0"],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
        ]
    ]
    vars << [
        grafana: [
            image: [name: "grafana/grafana", version: "6.0.0-beta1"],
            affinity: [key: "robobeerun.com/grafana", name: "required", required: true],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
            ingress: [enabled: true],
            hosts: "grafana.robobee-test.com",
            port: 3000,
            issuer: "selfsigning-issuer",
        ]
    ]
    vars << [
        kubeStateMetrics: [
            image: [name: "quay.io/coreos/kube-state-metrics", version: "v1.5.0"],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
        ]
    ]
    vars << [
        nodeExporter: [
            image: [name: "quay.io/prometheus/node-exporter", version: "v0.17.0"],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
        ]
    ]
    vars << [
        prometheusAdapter: [
            image: [name: "quay.io/coreos/k8s-prometheus-adapter-amd64", version: "v0.4.1"],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
        ]
    ]
    vars << [
        prometheus: [
            image: [name: "quay.io/prometheus/prometheus", version: "v2.5.0"],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
            allowOnMaster: false,
            replicas: 2,
            limits: [cpu: '100m', memory: '100Mi'],
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
