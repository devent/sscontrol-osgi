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
