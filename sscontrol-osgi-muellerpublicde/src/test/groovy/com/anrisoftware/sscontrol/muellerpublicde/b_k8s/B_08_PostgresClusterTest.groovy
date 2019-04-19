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
class B_08_PostgresClusterTest extends Abstract_Runner_Debian_Test {

    @Test
    void "cluster postgres from chart"() {
        def test = [
            name: "cluster postgres from chart",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
def vars = [
    replicas: 2,
    limits: [cpu: "0.5", memory: "200Mi"],
    requests: [cpu: "0.5", memory: "200Mi"],
]
service "from-helm", chart: "stable/postgresql", version: "3.15.0" with {
    release ns: "robobeerun-com-postgres", name: "robobeerun-com-postgres"
    config << """
image:
  registry: docker.io
  repository: ${postgresVars.image.name}
  tag: ${postgresVars.image.version}
volumePermissions:
  enabled: false
replication:
  enabled: true
  user: repl_user
  password: repl_password
  slaveReplicas: ${vars.replicas}
  ## Set synchronous commit mode: on, off, remote_apply, remote_write and local
  ## ref: https://www.postgresql.org/docs/9.6/runtime-config-wal.html#GUC-WAL-LEVEL
  synchronousCommit: "on"
  ## From the number of `slaveReplicas` defined above, set the number of those that will have synchronous replication
  ## NOTE: It cannot be > slaveReplicas
  numSynchronousReplicas: 1
  ## Replication Cluster application name. Useful for defining multiple replication policies
  applicationName: my_application
postgresqlUsername: ${postgresVars.admin.user}
postgresqlPassword: ${postgresVars.admin.password}
persistence:
  enabled: true
  size: 8Gi
master:
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: robobeerun.com/postgres-master
            operator: In
            values:
            - required
  tolerations:
  - effect: NoSchedule
    key: node-role.kubernetes.io/master
    operator: Equal
slave:
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: robobeerun.com/postgres-slave
            operator: In
            values:
            - required
  tolerations:
  - effect: NoSchedule
    key: node-role.kubernetes.io/master
    operator: Equal
resources:
  limits:
    cpu: ${vars.limits.cpu}
    memory: ${vars.limits.memory}
  requests:
    cpu: ${vars.requests.cpu}
    memory: ${vars.requests.memory}
metrics:
  enabled: false
  # resources: {}
  image:
    registry: docker.io
    repository: wrouesnel/postgres_exporter
    tag: v0.4.7
"""
}
''',
            scriptVars: [targetHosts: [masters: mastersHosts, nodes: nodesHosts], socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                postgresVars: postgresVars],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
