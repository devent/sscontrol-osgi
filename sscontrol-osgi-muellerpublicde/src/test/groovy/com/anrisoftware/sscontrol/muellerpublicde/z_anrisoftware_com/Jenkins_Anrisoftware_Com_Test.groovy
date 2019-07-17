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
import com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterMastersOnlySocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(AndreaClusterMastersOnlySocketCondition.class)
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
            ssh: [
                hosts: """anrisoftware.com ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCiaHO9JCCCsjIaE0FpkMaEUrQPan2sevgdNsD6DhJ8dBDDssxZ0tD4A5FnutUpZOaFqLK8ly/EtTSqdfCX5D47xNV4PQ+KrCzYPWCumSc98ZQEKlp8LPS1qcD4dsN6yNnCZvfyzLmg02Ih0PNLBsvOcYMM4TLxpVxFXfMNSv71P4cujYlL3/AqUpx6j6CEnf2jyScUweVf8JxNv8byXisSpe7XAp0RcacZBqOkyKXWl7cfnvdjrg8mCYX9wfuTohe/EnHux9+02jV/0isHxwwYNHq1rBLWuaq9L66vMf3ahBtN9Yxv59OrV5m7u58zc2zB/6gDOekZRiezYLzq1AMx
gitea.anrisoftware.com ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCiaHO9JCCCsjIaE0FpkMaEUrQPan2sevgdNsD6DhJ8dBDDssxZ0tD4A5FnutUpZOaFqLK8ly/EtTSqdfCX5D47xNV4PQ+KrCzYPWCumSc98ZQEKlp8LPS1qcD4dsN6yNnCZvfyzLmg02Ih0PNLBsvOcYMM4TLxpVxFXfMNSv71P4cujYlL3/AqUpx6j6CEnf2jyScUweVf8JxNv8byXisSpe7XAp0RcacZBqOkyKXWl7cfnvdjrg8mCYX9wfuTohe/EnHux9+02jV/0isHxwwYNHq1rBLWuaq9L66vMf3ahBtN9Yxv59OrV5m7u58zc2zB/6gDOekZRiezYLzq1AMx
[javadoc.anrisoftware.com]:30101 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDp3JH9OlPnJvr247mF0PIkEL5jGah5CnOkU5MsPfN4rQVWCnCTlsfLxGUdqex2OYfPz/T0XDrK4+U3fx/tdl/va8m+gdZbf5gIPISfd/7ugvyACUSIP2q6ReintqGagwhrqNykP7oemVL98zaBbIxk0SzJw1vx0ipincTsvaJik5+CX3WqCqiAHVYBi5Bly2vYng/Za5rLA/BJKIKoM3JlZbp0vkAeboCAL4/kWEsssjVbL6GUgpvQJQNFpSTbaG/Me7oN0xZgUMAGtCbWULuj7sFadBBYuUanzDzNPxCCCKoggcff4hgAxlh5wsIK4lnxqLKuLjNg8h6U5VcVysCz
""",
                id_rsa: jenkinsIdRsa,
                user: "jenkins",
                pass: "",
            ],
            volume: [storage: "2Gi"]
        ]
    ]
    vars << [
        nginx: [
            clientMaxBodySize: '64m',
        ],
    ]
}
def vars = [
    limits: [cpu: "0.5", memory: "1Gi"],
    requests: [cpu: "0.5", memory: "1Gi"],
]
/*service "from-helm", chart: "stable/jenkins", version: "1.1.23" with {
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
    - kubernetes:1.15.6
    - workflow-job:2.32
    - workflow-aggregator:2.6
    - credentials-binding:1.19
    - git:3.10.0
    - oic-auth:1.6
    - matrix-auth:2.4.2
    - sonar:2.9
    - config-file-provider:3.6
    - gitea:1.1.2
    - pipeline-maven:3.7.1
    - junit-attachments:1.5
    - jacoco:3.0.4
    - warnings-ng:5.1.0
    - pipeline-utility-steps:2.3.0
    - groovy-postbuild:2.4.3
    - embeddable-build-status:2.0.1
  podLabels:
    app: jenkins
  healthProbeLivenessInitialDelay: 600
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
}*/
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                jenkinsIdRsa: AndreaClusterMastersOnlySocketCondition.class.getResource("jenkins_id_rsa.txt"),
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
