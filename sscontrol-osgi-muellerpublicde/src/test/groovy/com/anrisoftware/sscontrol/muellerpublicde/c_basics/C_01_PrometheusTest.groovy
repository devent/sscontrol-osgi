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
package com.anrisoftware.sscontrol.muellerpublicde.c_basics

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
class C_01_PrometheusTest extends Abstract_Runner_Debian_Test {

    @Test
    void "prometheus"() {
        def test = [
            name: "prometheus",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "kube-prometheus" with {
    remote url: "git@github.com:robobee-repos/kube-prometheus.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/v0.29.0-k8s1.13"
}
service "from-repository", repo: "kube-prometheus", dryrun: false with {
    vars << [
        prometheusOperator: [
            limits: [cpu: '200m', memory: '200Mi'],
            requests: [cpu: '200m', memory: '200Mi'],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
			allowOnMaster: true,
        ]
    ]
    vars << [
        grafana: [
            image: [name: 'grafana/grafana', version: '6.0.1'],
            limits: [cpu: '200m', memory: '200Mi'],
            requests: [cpu: '200m', memory: '200Mi'],
			revision: "r6",
            affinity: [key: "robobeerun.com/grafana", name: "required", required: true],
			allowOnMaster: false,
			storage: [size: "1Gi"],
			port: 3000,
            hosts: ['grafana.andrea.muellerpublic.de'],
			issuer: "letsencrypt-prod",
			oauth: [generic: [
			    enabled: true,
				allowSignUp: false,
				clientId: "grafana.andrea.muellerpublic.de",
				clientSecret: "028d12c0-f277-4913-aa31-9a7ee27ad29a",
				scopes: "openid profile email",
				authUrl: "https://sso.andrea.muellerpublic.de/auth/realms/public/protocol/openid-connect/auth",
				tokenUrl: "https://sso.andrea.muellerpublic.de/auth/realms/public/protocol/openid-connect/token",
				apiUrl: "https://sso.andrea.muellerpublic.de/auth/realms/public/protocol/openid-connect/userinfo",
			] ],
			email: [enabled: true, host: emailVars.host, port: emailVars.port, user: "alerts@muellerpublic.de", password: "Thie6theike4choh"],
        ]
    ]
    vars << [
        kubeStateMetrics: [
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
			allowOnMaster: true,
        ]
    ]
    vars << [
        nodeExporter: [
            limits: [cpu: '250m', memory: '180Mi'],
            requests: [cpu: '250m', memory: '180Mi'],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
			allowOnMaster: true,
        ]
    ]
    vars << [
        prometheusAdapter: [
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
			allowOnMaster: true,
        ]
    ]
    vars << [
        prometheus: [
            limits: [cpu: '250m', memory: '800Mi'],
            requests: [cpu: '250m', memory: '800Mi'],
            affinity: [key: "robobeerun.com/prometheus", name: "required", required: true],
			allowOnMaster: true,
			replicas: 1,
			storage: [size: "5Gi"],
			port: 9090,
            hosts: ['prometheus.andrea.muellerpublic.de'],
			issuer: "letsencrypt-prod",
        ]
    ]
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                emailVars: emailVars,
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
