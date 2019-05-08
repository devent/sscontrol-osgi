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
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
class B_04_NginxIngressInstallTest extends Abstract_Runner_Debian_Test {

    @Test
    void "nginx-ingress"() {
        def test = [
            name: "nginx-ingress",
            script: '''
def edgeNodeTargetHttpPort = 30000
def edgeNodeTargetHttpsPort = 30001
def edgeNodeTargetSshPort = 30022

service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "ssh", group: "edge-nodes" with {
    host targetHosts.nodes[0], socket: socketFiles.nodes[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "nginx-ingress" with {
    remote url: "git@github.com:robobee-repos/kube-nginx-ingress.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "release/nginx-0.23.0-r.1"
}
service "from-repository", repo: "nginx-ingress", dest: "/etc/kubernetes/addons/ingress-nginx" with {
    vars << [
        nginxIngressController: [
            image: [name: 'quay.io/kubernetes-ingress-controller/nginx-ingress-controller', version: '0.23.0'],
            limits: [cpu: '0', memory: '200Mi'],
            requests: [cpu: '0', memory: '200Mi'],
            affinity: [key: "robobeerun.com/ingress-nginx", name: "required", required: true],
            nodePort: [http: edgeNodeTargetHttpPort, https: edgeNodeTargetHttpsPort],
            config: [
                useProxyProtocol: true,
            ]
        ]
    ]
    vars << [
        defaultHttpBackend: [
            image: [name: 'gcr.io/google_containers/defaultbackend', version: '1.4'],
            limits: [cpu: '10m', memory: '20Mi'],
            requests: [cpu: '10m', memory: '20Mi'],
            affinity: [key: "robobeerun.com/ingress-nginx", name: "required", required: true],
        ]
    ]
}
def edgeTargetAddress = targets["edge-nodes"].head().hostAddress
service "haproxy", version: "1.8", target: "edge-nodes" with {
    proxy "http" with {
        frontend address: edgeTargetAddress
        backend address: edgeTargetAddress, port: 30000
    }
    proxy "https" with {
        frontend address: edgeTargetAddress
        backend address: edgeTargetAddress, port: 30001
    }
    proxy "ssh" with {
        frontend address: edgeTargetAddress
        backend address: edgeTargetAddress, port: 30022
    }
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
