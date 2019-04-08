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
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@ExtendWith(ClusterTestMastersNodesSocketCondition.class)
class B_07_MariadbClusterTest extends Abstract_Runner_Debian_Test {

    @Test
    void "cluster mariadb from chart"() {
        def test = [
            name: "cluster mariadb from chart",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
def vars = [
  root: [password: 'wooSh6ohrah2AiCiY7uchoh5Leisee4x'],
  replicator: [password: 'eir7Shoom5eidoobaequo6ReeSie1ohn'],
  master: [resources: [limits: [cpu: "0.5", memory: "200Mi"], requests: [cpu: "0.5", memory: "200Mi"]]],
  slave: [replicas: 2, resources: [limits: [cpu: "0.5", memory: "200Mi"], requests: [cpu: "0.5", memory: "200Mi"]]],
]
service "from-helm", chart: "stable/mariadb", version: "5.11.0" with {
    release ns: "robobeerun-com-mariadb", name: "robobeerun-com-mariadb"
    config << """
image:
  registry: docker.io
  repository: bitnami/mariadb
  tag: 10.1.38
rootUser:
  password: ${vars.root.password}
replication:
  enabled: true
  user: replicator
  password: ${vars.replicator.password}
  forcePassword: true
master:
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: robobeerun.com/mariadb-master
            operator: In
            values:
            - required
  tolerations:
  - effect: NoSchedule
    key: node-role.kubernetes.io/master
    operator: Equal
  persistence:
    enabled: true
    size: 8Gi
  resources:
    limits:
      cpu: ${vars.master.resources.limits.cpu}
      memory: ${vars.master.resources.limits.memory}
    requests:
      cpu: ${vars.master.resources.requests.cpu}
      memory: ${vars.master.resources.requests.memory}
slave:
  replicas: ${vars.slave.replicas}
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: robobeerun.com/mariadb-slave
            operator: In
            values:
            - required
  tolerations:
  - effect: NoSchedule
    key: node-role.kubernetes.io/master
    operator: Equal
  persistence:
    size: 8Gi
  resources:
    limits:
      cpu: ${vars.slave.resources.limits.cpu}
      memory: ${vars.slave.resources.limits.memory}
    requests:
      cpu: ${vars.slave.resources.requests.cpu}
      memory: ${vars.slave.resources.requests.memory}
metrics:
  enabled: false
  image:
    registry: docker.io
    repository: prom/mysqld-exporter
    tag: v0.11.0
  resources:
    limits:
      cpu: 0.2
      memory: 20Mi
    requests:
      cpu: 0.2
      memory: 20Mi
  annotations:
    prometheus.io/scrape: "true"
prometheus.io/port: "9104"
"""
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
