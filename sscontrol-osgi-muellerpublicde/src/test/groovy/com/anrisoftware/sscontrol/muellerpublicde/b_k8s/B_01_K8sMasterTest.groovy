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
import static com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterMastersNodesSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(AndreaClusterMastersNodesSocketCondition.class)
class B_01_K8sMasterTest extends Abstract_Runner_Debian_Test {

    @Test
    void "masters"() {
        def test = [
            name: "masters",
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
service "k8s-master", target: 'masters', name: "andrea-node-0.muellerpublic.de" with {
    bind secure: target.hostAddress
    kubelet address: target.hostAddress
    nodes << "masters"
    nodes << "nodes"
    plugin "canal", iface: mainNetwork
    plugin "etcd", endpoint: "https://10.10.10.7:22379" with {
        tls k8sVars.etcd.certs
    }
    label.addAll k8sVars.nodes[0].labels
    taint.addAll k8sVars.nodes[0].taints
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, mainNetwork: mainNetwork],
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}