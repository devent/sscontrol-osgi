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
class A_04_EtcdTest extends Abstract_Runner_Debian_Test {

    @Test
    void "etcd"() {
        def test = [
            name: "etcd",
            script: '''
service "ssh", group: "etcd" with {
    host targetHosts[0], socket: socketFiles.masters[0]
    host targetHosts[1], socket: socketFiles.nodes[0]
    host targetHosts[2], socket: socketFiles.nodes[1]
}
targets.etcd.eachWithIndex { host, i ->
    service "etcd", target: host, member: "etcd-${i}", check: targets.etcd[-1] with {
        debug "debug", level: 1
        bind "https://${host.hostAddress}:2379"
        advertise "https://etcd-${i}.robobee-test.test:2379"
        client etcdVars.client
        tls cert: etcdVars.nodes[i].cert, key: etcdVars.nodes[i].key
        authentication "cert", ca: etcdVars.nodes[i].ca
        peer state: "new", advertise: "https://etcd-${i}.robobee-test.test:2380", listen: "https://${host.hostAddress}:2380", token: "XuSeich3" with {
            cluster << "etcd-0=https://etcd-0.robobee-test.test:2380"
            cluster << "etcd-1=https://etcd-1.robobee-test.test:2380"
            cluster << "etcd-2=https://etcd-2.robobee-test.test:2380"
            tls cert: etcdVars.nodes[i].cert, key: etcdVars.nodes[i].key
            authentication "cert", ca: etcdVars.nodes[i].ca
        }
        property << "archive_ignore_key=true"
    }
}
targets.etcd.eachWithIndex { host, i ->
    service "etcd", target: host, check: targets.etcd[-1] with {
        bind network: "${mainNetwork}:1", "https://10.10.10.7:22379"
        gateway endpoints: "https://etcd-${i}.robobee-test.test:2379"
        client etcdVars.client
        property << "archive_ignore_key=true"
    }
}
''',
            scriptVars: [targetHosts: etcdHosts, socketFiles: socketFiles, etcdVars: etcd_vars, mainNetwork: mainNetwork],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
