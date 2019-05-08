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
import static com.anrisoftware.sscontrol.muellerpublicde.zz_cluster_test.ClusterTestResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.zz_cluster_test.ClusterTestMastersNodesSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
class A_05_NfsTest extends Abstract_Runner_Debian_Test {

    @Test
    void "nfs"() {
        def test = [
            name: "nfs",
            script: '''
service "ssh", group: "nfs" with {
    host targetHosts[0], socket: socketFiles.nfs[0]
}
service "ssh", group: "exports" with {
    host targetHosts[0], socket: socketFiles.nfs[0]
    host targetHosts[1], socket: socketFiles.nfs[1]
    host targetHosts[2], socket: socketFiles.nfs[2]
}
addresses = targets.exports.inject([]) { list, it -> list << it.hostAddress }
service "nfs", version: "1.3", target: "nfs" with {
    export dir: "/nfsfileshare/0" with {
        host << addresses[0]
        host << addresses[1]
        host << addresses[2]
    }
}
''',
            scriptVars: [targetHosts: nfsHosts, socketFiles: socketFiles],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
