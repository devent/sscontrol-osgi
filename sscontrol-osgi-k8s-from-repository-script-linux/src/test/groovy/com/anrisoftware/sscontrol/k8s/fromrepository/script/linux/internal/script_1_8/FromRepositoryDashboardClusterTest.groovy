package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FromRepositoryDashboardClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "dashboard addon to cluster"() {
        def test = [
            name: "dashboard_cluster",
            script: '''
service "ssh", host: "andrea-master.robobee-test.test", socket: robobeeSocket
service "k8s-cluster"
service "repo-git", group: "dashboard" with {
    remote url: "git@github.com:robobee-repos/dashboard.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "dashboard", dest: "/etc/kubernetes/addons/dashboard" with {
    vars << [
        dashboard: [
            image: [name: "k8s.gcr.io/kubernetes-dashboard-amd64", version: "v1.8.3"],
            affinity: [key: "robobeerun.com/dashboard", name: "required", required: true],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [],
        ]
    ]
}
''',
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @BeforeEach
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
