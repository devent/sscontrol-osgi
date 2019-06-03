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
package com.anrisoftware.sscontrol.muellerpublicde.z_anrisoftware_com

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
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(AndreaClusterMastersNodesSocketCondition.class)
class Jenkins_Anrisoftware_Com_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "jenkins from chart"() {
        def test = [
            name: "jenkins",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "jenkins-anrisoftware-com-deploy" with {
    remote url: "git@github.com:robobee-repos/jenkins-anrisoftware-com-deploy.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "master"
}
service "from-repository", repo: "jenkins-anrisoftware-com-deploy" with {
    vars << [
        jenkins: [
            hosts: [
                'jenkins.anrisoftware.com', // main domain
            ],
            issuer: "letsencrypt-prod",
        ]
    ]
    vars << [
        nginx: [
            clientMaxBodySize: '64m',
        ],
    ]
}
def vars = [
    replicas: 2,
    limits: [cpu: "0.20", memory: "500Mi"],
    requests: [cpu: "0.20", memory: "500Mi"],
]
service "from-helm", chart: "stable/jenkins", version: "1.1.23" with {
    release ns: "jenkins-anrisoftware-com", name: "jenkins-anrisoftware-com"
    config << """
master:
  adminUser: "fao9Thuy"
  adminPassword: "ju8poos3ahGae7Fa"
  resources:
    limits:
      cpu: ${vars.limits.cpu}
      memory: ${vars.limits.memory}
    requests:
      cpu: ${vars.requests.cpu}
      memory: ${vars.requests.memory}
  javaOpts: "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
  usePodSecurityContext: true
  runAsUser: 1000
  fsGroup: 1000
  servicePort: 8080
  targetPort: 8080
  serviceType: ClusterIP
  installPlugins:
    - kubernetes:1.15.5
    - workflow-job:2.32
    - workflow-aggregator:2.6
    - credentials-binding:1.19
    - git:3.10.0
    - oic-auth:1.6
    - matrix-auth:2.4.2
  podLabels:
    app: jenkins
  healthProbeLivenessInitialDelay: 200
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: jenkins.anrisoftware.com
            operator: In
            values:
            - required
agent:
  resources:
    requests:
      cpu: "200m"
      memory: "500Mi"
    limits:
      cpu: "200m"
      memory: "500Mi"
  yamlTemplate: |-
    apiVersion: v1
    kind: Pod
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
              - key: jenkins.anrisoftware.com
                operator: In
                values:
                - required
persistence:
  enabled: true
  accessMode: "ReadWriteOnce"
  size: "8Gi"

"""
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
            ],
            expectedServicesSize: 5,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
