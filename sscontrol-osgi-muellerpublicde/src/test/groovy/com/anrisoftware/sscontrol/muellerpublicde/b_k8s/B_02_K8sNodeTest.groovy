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
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
class B_02_K8sNodeTest extends Abstract_Runner_Debian_Test {

    @Test
    void "install nodes"() {
        def test = [
            name: "install nodes",
            script: '''
service "ssh", group: "masters" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "ssh", group: "nodes" with {
    host targetHosts.nodes[0], socket: socketFiles.nodes[0]
    host targetHosts.nodes[1], socket: socketFiles.nodes[1]
}
service "k8s-cluster", target: 'masters' with {
}
targets.nodes.eachWithIndex { host, i ->
    service "k8s-node", target: host, name: "andrea-node-${i+1}.muellerpublic.de" with {
        plugin "nfs-client"
        kubelet address: target.hostAddress
        cluster host: "masters", join: k8sVars.joinCommand
        label.addAll k8sVars.nodes[i+1].labels
        taint.addAll k8sVars.nodes[i+1].taints
        property << "fail_swap_on=false"
    }
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars],
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
