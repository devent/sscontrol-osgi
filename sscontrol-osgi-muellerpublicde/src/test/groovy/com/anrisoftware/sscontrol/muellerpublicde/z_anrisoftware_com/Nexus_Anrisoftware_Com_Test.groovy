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
class Nexus_Anrisoftware_Com_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "nexus from chart"() {
        def test = [
            name: "nexus",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "nexus-anrisoftware-com-deploy" with {
    remote url: "git@github.com:robobee-repos/nexus-anrisoftware-com-deploy.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "master"
}
def v = [
    limits: [cpu: "0.20", memory: "1500Mi"],
    requests: [cpu: "0.20", memory: "1500Mi"],
]
service "from-repository", repo: "nexus-anrisoftware-com-deploy" with {
    vars << [
        nexus: [
            hosts: [
                'maven.anrisoftware.com', // main domain
            ],
            port: 8080,
            issuer: "letsencrypt-prod",
        ]
    ]
    vars << [
        nginx: [
            clientMaxBodySize: '64m',
        ],
    ]
}
service "from-helm", chart: "stable/sonatype-nexus", version: "1.18.3" with {
    release ns: "nexus-anrisoftware-com", name: "nexus-anrisoftware-com"
    config << """
replicaCount: 1
nexus:
  imageName: erwin82/nexus
  imageTag: travelaudience-docker-nexus-3.15.2-r.1
  env:
    - name: install4jAddVmParams
      value: "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
  resources:
    limits:
      cpu: ${v.limits.cpu}
      memory: ${v.limits.memory}
    requests:
      cpu: ${v.requests.cpu}
      memory: ${v.requests.memory}
  service:
    type: ClusterIP
  securityContext:
    fsGroup: 2000
  livenessProbe:
    initialDelaySeconds: 600
    path: /
  readinessProbe:
    initialDelaySeconds: 600
    path: /
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: nexus.anrisoftware.com
            operator: In
            values:
            - required
nexusProxy:
  enabled: true
  env:
    nexusDockerHost: "maven.anrisoftware.com"
    nexusHttpHost: "maven.anrisoftware.com"
    enforceHttps: true
    cloudIamAuthEnabled: false
## If cloudIamAuthEnabled is set to true uncomment the variables below and remove this line
  #   clientId: ""
  #   clientSecret: ""
  #   organizationId: ""
  #   redirectUrl: ""
  #   requiredMembershipVerification: "true"
  # secrets:
  #   keystore: ""
  #   password: ""
  resources:
    requests:
      cpu: 100m
      memory: 250Mi
    limits:
      cpu: 200m
      memory: 250Mi
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
