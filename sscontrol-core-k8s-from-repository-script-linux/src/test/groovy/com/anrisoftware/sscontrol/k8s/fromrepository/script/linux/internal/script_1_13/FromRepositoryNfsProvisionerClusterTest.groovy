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
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(Nodes3Port22222AvailableCondition.class)
class FromRepositoryNfsProvisionerClusterTest extends AbstractFromRepositoryRunnerTest {

    @Test
    void "nfs client addon to cluster"() {
        def test = [
            name: "cluster_nfs_addon_cluster",
            script: '''
service "ssh", host: "robobee@node-0.robobee-test.test", socket: sockets.masters[0]
service "k8s-cluster"
service "repo-git", group: "nfs" with {
    remote url: "git@github.com:robobee-repos/kube-nfs-provisioner.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/v5.2.0-k8s1.13-r.1"
}
service "from-repository", repo: "nfs", dest: "/etc/kubernetes/addons/nfs" with {
    vars << [
        nfs: [
            image: [name: "quay.io/external_storage/nfs-client-provisioner", version: "v3.1.0-k8s1.11"],
            affinity: [key: "robobeerun.com/nfs", name: "required", required: true],
            allowOnMaster: true,
            limits: [cpu: '100m', memory: '100Mi'],
            options: [default: true, archiveOnDelete: true],
            server: [address: "192.168.56.200", export: "/nfsfileshare/0"],
        ]
    ]
}
''',
            scriptVars: [sockets: nodesSockets, robobeeKey: robobeeKey],
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
