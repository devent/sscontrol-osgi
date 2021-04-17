/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_13

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(RobobeeSocketCondition.class)
class FromHelmPostgresClusterTest extends AbstractFromHelmRunnerTest {

    @Test
    void "cluster postgres from chart"() {
        def test = [
            name: "cluster_postgres_chart",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
def vars = [
  root: [password: 'oophiReighiela6choo1eiWainguliej'],
  resources: [limits: [cpu: "0.5", memory: "200Mi"], requests: [cpu: "0.5", memory: "200Mi"]],
]
service "from-helm", chart: "stable/postgresql", version: "3.15.0" with {
    release ns: "robobeerun-com-postgres", name: "postgres"
    config << """
image:
  registry: docker.io
  repository: bitnami/postgresql
  tag: 10.7.0
replication:
  enabled: true
  user: repl_user
  password: repl_password
  slaveReplicas: 2
  ## Set synchronous commit mode: on, off, remote_apply, remote_write and local
  ## ref: https://www.postgresql.org/docs/9.6/runtime-config-wal.html#GUC-WAL-LEVEL
  synchronousCommit: "on"
  ## From the number of `slaveReplicas` defined above, set the number of those that will have synchronous replication
  ## NOTE: It cannot be > slaveReplicas
  numSynchronousReplicas: 1
  ## Replication Cluster application name. Useful for defining multiple replication policies
  applicationName: my_application
postgresqlUsername: postgres
postgresqlPassword: ${vars.root.password}
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
    cpu: ${vars.resources.limits.cpu}
    memory: ${vars.resources.limits.memory}
  requests:
    cpu: ${vars.resources.requests.cpu}
    memory: ${vars.resources.requests.memory}
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
            scriptVars: [
                robobeeSocket: robobeeSocket,
                robobeeKey: robobeeKey,
                robobeePub: robobeePub,
            ],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
