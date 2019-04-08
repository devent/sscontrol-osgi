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
class B_03_K8sAddonManagerTest extends Abstract_Runner_Debian_Test {

    @Test
    void "andrea-mueller use host"() {
        def test = [
            name: "andrea-mueller use host",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "kube-addon-manager" with {
    remote url: "git@github.com:robobee-repos/kube-addon-manager.git"
    credentials "ssh", key: robobeeKey
}
service "from-repository", repo: "kube-addon-manager" with {
    vars << [
        addonManager: [
            image: [name: 'k8s.gcr.io/kube-addon-manager', version: 'v9.0'],
            limits: [cpu: '0.2', memory: '100Mi'],
            requests: [cpu: '0.2', memory: '100Mi'],
        ]
    ]
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
