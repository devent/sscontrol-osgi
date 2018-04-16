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
class FromRepositoryHeapsterGrafanaEphemeralClusterTest extends AbstractFromRepositoryRunnerTest {

    static final Map certs = [
        worker: [
            ca: FromRepositoryManifestsServerTest.class.getResource('robobee_test_kube_ca.pem'),
            cert: FromRepositoryManifestsServerTest.class.getResource('robobee_test_kube_admin_cert.pem'),
            key: FromRepositoryManifestsServerTest.class.getResource('robobee_test_kube_admin_key.pem'),
        ],
    ]

    @Test
    void "heapster_grafana_ephemeral"() {
        def test = [
            name: "heapster_grafana_ephemeral",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', ca: certs.worker.ca, cert: certs.worker.cert, key: certs.worker.key
}
service "repo-git", group: "heapster-influxdb-grafana-monitoring" with {
    remote url: "git@github.com:robobee-repos/heapster-influxdb-grafana-monitoring.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "heapster-influxdb-grafana-monitoring", dest: "/etc/kubernetes/addons/cluster-monitoring" with {
    vars << [
        heapster: [
            image: [name: "k8s.gcr.io/heapster-amd64", version: "v1.5.2"],
            affinity: [key: "robobeerun.com/heapster", name: "required", required: true],
            allowOnMaster: true
        ]
    ]
    vars << [
        eventer: [
            baseCpu: '25m',
            extraCpu: '0',
            baseMemory: '40Mi',
            extraMemory: '5Mi',
        ]
    ]
    vars << [
        resizer: [
            image: [name: 'k8s.gcr.io/addon-resizer', version: '1.8.1']
        ]
    ]
    vars << [
        nanny: [
            limits: [cpu: '25m', memory: '60Mi'],
            requests: [cpu: '25m', memory: '60Mi'],
        ]
    ]
    vars << [
        metrics: [
            baseCpu: '25m',
            extraCpu: '0',
            baseMemory: '40Mi',
            extraMemory: '5Mi',
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
            limits: [cpu: '50m', memory: '100Mi'],
            requests: [cpu: '50m', memory: '100Mi'],
        ]
    ]
    vars << [
        grafana: [
            image: [name: 'k8s.gcr.io/heapster-grafana-amd64', version: 'v4.4.3'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            auth: [basic: true, anonymous: false],
        ]
    ]
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
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
