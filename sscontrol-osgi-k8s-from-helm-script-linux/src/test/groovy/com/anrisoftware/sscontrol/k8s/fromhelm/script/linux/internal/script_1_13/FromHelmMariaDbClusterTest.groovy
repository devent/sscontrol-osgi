/*-
 * #%L
 * sscontrol-osgi - k8s-from-helm-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
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
class FromHelmMariaDbClusterTest extends AbstractFromHelmRunnerTest {

    @Test
    void "cluster mariadb from chart"() {
        def test = [
            name: "cluster_mariadb_chart",
            script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "from-helm", chart: "stable/mariadb", version: "5.11.0" with {
    release ns: "mariadb", name: "mariadb"
    config << """
image:
  registry: docker.io
  repository: bitnami/mariadb
  tag: 10.1.38
rootUser:
  password:
replication:
  enabled: true
  user: replicator
  password:
  forcePassword: true
master:
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: robobeerun.com/mariadb
            operator: In
            values:
            - required
  tolerations: []
  persistence:
    enabled: true
    size: 8Gi
  resources: {}
slave:
  replicas: 2
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: robobeerun.com/mariadb
            operator: In
            values:
            - required
  persistence:
    size: 8Gi
  resources: {}
metrics:
  enabled: true
  image:
    registry: docker.io
    repository: prom/mysqld-exporter
    tag: v0.10.0
  resources: {}
  annotations:
    prometheus.io/scrape: "true"
prometheus.io/port: "9104"
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
