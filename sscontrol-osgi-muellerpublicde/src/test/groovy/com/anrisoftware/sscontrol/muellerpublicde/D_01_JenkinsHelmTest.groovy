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
package com.anrisoftware.sscontrol.muellerpublicde

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class D_01_JenkinsHelmTest extends Abstract_Runner_Debian_Test {

    @Test
    void "install wordpress using Helm chart"() {
        def test = [
            name: "helm-andrea",
            script: '''
service "ssh" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "from-helm", chart: "stable/jenkins", version: "0.16.20" with {
    release ns: "jenkins", name: "jenkins"
    config << """
Persistence:
  StorageClass: "managed-nfs-storage"
  Size: 20Gi
rbac:
  install: true
Master:
  Cpu: 200m
  Memory: 400Mi
  RunAsUser: 1000
  FsGroup: 1000
  ServiceType: ClusterIP
  HostName: jenkins.anrisoftware.com
  Ingress:
    Annotations:
      kubernetes.io/ingress.class: nginx
      nginx.ingress.kubernetes.io/affinity: cookie
      nginx.ingress.kubernetes.io/session-cookie-hash: sha1
      nginx.ingress.kubernetes.io/session-cookie-name: route
      nginx.org/client-max-body-size: 64m
    TLS:
    - hosts:
      - jenkins.anrisoftware.com
      secretName: jenkins-anrisoftware-com-tls
  Affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: muellerpublic.de/anrisoftware-com
            operator: In
            values:
            - required
    podAffinity:
      preferredDuringSchedulingIgnoredDuringExecution:
      - podAffinityTerm:
          labelSelector:
            matchExpressions:
            - key: muellerpublic.de/anrisoftware-com
              operator: In
              values:
              - required
          topologyKey: kubernetes.io/hostname
        weight: 100
"""
    config << """
Agent:
  Cpu: 200m
  Memory: 400Mi
  NodeSelector:
    muellerpublic.de/anrisoftware-com: required
"""
}
''',
            scriptVars: [socketFiles: socketFiles, k8s_vars: k8s_vars, robobeeKey: robobeeKey],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeMastersExists()
        assumeNodesExists()
    }
}
