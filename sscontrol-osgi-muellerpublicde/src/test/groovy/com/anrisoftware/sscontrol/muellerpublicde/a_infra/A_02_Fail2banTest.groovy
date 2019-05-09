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
package com.anrisoftware.sscontrol.muellerpublicde.a_infra

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
//@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
@ExtendWith(AndreaClusterMastersNodesSocketCondition.class)
class A_02_Fail2banTest extends Abstract_Runner_Debian_Test {

    @Test
    void "setup firewall and fail2ban on node ports"() {
        def test = [
            name: "fail2ban",
            script: '''
service "ssh", group: "masters" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "ssh", group: "nodes" with {
    host targetHosts.nodes[0], socket: socketFiles.nodes[0]
    host targetHosts.nodes[1], socket: socketFiles.nodes[1]
}
service "fail2ban", target: "masters" with {
    banning time: "P7D"
    jail "sshd"
}
service "fail2ban", target: "nodes" with {
    banning time: "P7D"
    jail "sshd", port: nodesSshPort
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, nodesSshPort: nodesSshPort],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
