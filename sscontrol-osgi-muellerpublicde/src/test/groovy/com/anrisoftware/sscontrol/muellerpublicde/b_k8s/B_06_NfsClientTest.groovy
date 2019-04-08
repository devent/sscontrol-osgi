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
import static com.anrisoftware.sscontrol.muellerpublicde.cluster_test.ClusterTestResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.cluster_test.ClusterTestMastersNodesSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
class B_06_NfsClientTest extends Abstract_Runner_Debian_Test {

    @Test
    void "nfs-client"() {
        def test = [
            name: "nfs-client",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "nfs" with {
    remote url: "git@github.com:robobee-repos/kube-nfs-provisioner.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/v5.2.0-k8s1.13-r.1"
}
service "nfs", version: "1.3" with {
    export dir: "/nfsfileshare/0" with {
        host << "192.168.56.200"
        host << "192.168.56.201"
        host << "192.168.56.202"
    }
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
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 5,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
