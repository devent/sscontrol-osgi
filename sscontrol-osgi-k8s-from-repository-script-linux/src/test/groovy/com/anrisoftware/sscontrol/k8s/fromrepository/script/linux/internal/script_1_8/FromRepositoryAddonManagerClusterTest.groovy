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
class FromRepositoryAddonManagerClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "deploy addon-manager to cluster"() {
        def test = [
            name: "cluster_addon_manager",
            script: '''
service "ssh", host: "robobee@node-0.robobee-test.test", socket: sockets.masters[0]
service "k8s-cluster"
service "repo-git", group: "kube-addon-manager" with {
    remote url: "git@github.com:robobee-repos/kube-addon-manager.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "kube-addon-manager" with {
    vars << [
        addonManager: [
            image: [name: 'k8s.gcr.io/kube-addon-manager', version: 'v8.6'],
            limits: [cpu: '0.25', memory: '50Mi'],
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
