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
class FromRepositoryHeapsterGrafanaPersistentClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "heapster_grafana_persistent"() {
        def test = [
            name: "heapster_grafana_persistent",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: sockets.masters[0]
service "k8s-cluster"
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
            baseCpu: '25m', extraCpu: '0', baseMemory: '40Mi', extraMemory: '5Mi',
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
            requests: [],
        ]
    ]
    vars << [
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
            limits: [cpu: '50m', memory: '100Mi'],
            requests: [],
            persistent: [share: true, storage: [class: "slow", size: "1Gi"]],
        ]
    ]
    vars << [
        grafana: [
            image: [name: 'k8s.gcr.io/heapster-grafana-amd64', version: 'v4.4.3'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [],
            auth: [basic: true, anonymous: false],
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
