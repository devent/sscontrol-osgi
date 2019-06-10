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
class Sonar_Anrisoftware_Com_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "sonarqube from chart"() {
        def test = [
            name: "sonarqube",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "sonar-anrisoftware-com-deploy" with {
    remote url: "git@github.com:robobee-repos/sonar-anrisoftware-com-deploy.git"
    credentials "ssh", key: robobeeKey
    checkout branch: "master"
}
def v = [
    limits: [cpu: "0.20", memory: "2000Mi"],
    requests: [cpu: "0.20", memory: "2000Mi"],
    db: [revision: "r1", database: "sonardb", user: "sonardb", password: "Ohleisheeshuok8nipet6bey0nohg1ni",],
]
service "from-repository", repo: "sonar-anrisoftware-com-deploy" with {
    vars << [
        db: [
            image: postgresVars.image,
            admin: postgresVars.admin,
            host: postgresVars.host,
            port: postgresVars.port,
            database: v.db.database, user: v.db.user, password: v.db.password,
            schema: "public",
            revision: v.db.revision,
            type: "postgres",
        ]
    ]
    vars << [
        sonar: [
            hosts: [
                'sonar.anrisoftware.com', // main domain
            ],
            port: 9000,
            issuer: "letsencrypt-prod",
        ]
    ]
    vars << [
        nginx: [
            clientMaxBodySize: '64m',
        ],
    ]
}
service "from-helm", chart: "stable/sonarqube", version: "1.0.4" with {
    release ns: "sonar-anrisoftware-com", name: "sonar-anrisoftware-com"
    config << """
service:
  name: sonarqube
  type: ClusterIP
  externalPort: 9000
  internalPort: 9000
affinity:
  nodeAffinity:
    requiredDuringSchedulingIgnoredDuringExecution:
      nodeSelectorTerms:
      - matchExpressions:
        - key: sonar.anrisoftware.com
          operator: In
          values:
          - required
readinessProbe:
  initialDelaySeconds: 300
livenessProbe:
  initialDelaySeconds: 300
extraEnv:
  sonar.web.javaAdditionalOpts: "-server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
  sonar.search.javaAdditionalOpts: "-server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
resources:
  limits:
    cpu: ${v.limits.cpu}
    memory: ${v.limits.memory}
  requests:
    cpu: ${v.requests.cpu}
    memory: ${v.requests.memory}
persistence:
  enabled: true
  accessMode: "ReadWriteOnce"
  size: "8Gi"
plugins:
  install: []
database:
  type: "postgresql"
postgresql:
  enabled: false
  postgresServer: "${postgresVars.host}"
  postgresUser: "${v.db.user}"
  postgresPasswordSecret: "db-${v.db.revision}"
  postgresDatabase: "${v.db.database}"
podLabels:
  app: sonar
"""
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                postgresVars: postgresVars
            ],
            expectedServicesSize: 5,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
