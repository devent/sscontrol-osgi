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
class C_01_ElasticKibanaTest extends Abstract_Runner_Debian_Test {

    @Test
    void "elastic-kibana"() {
        def test = [
            name: "elastic-kibana",
            script: '''
service "ssh" with {
    host targetHosts.masters[0], socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "anrisoftware-com" with {
    remote url: "git@github.com:robobee-repos/kube-elastic-kibana.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "master"
}
service "from-repository", repo: "anrisoftware-com", dryrun: false with {
    vars << [
        elastic: [
            image: [name: "amazon/opendistro-for-elasticsearch", version: "0.9.0"],
            revision: "r12",
            cert: elasticVars.cert,
            users: [
                admin: [password: 'wootha3aeJuThuum', hash: '$2y$12$9wTljr.XL6PpAdVy0FYXxuuoKkp0W7CMvZk6EhbBEKOtw6DgPNi1C'],
                logstash: [password: 'oiDo8ohchah9uox5', hash: '$2y$12$fu3tt8WO8nJXnm9sgSCYJO.mNDD5NfMlxpdYa1nPYLD7s3GZpKxgm'],
                kibanaserver: [password: 'ca9Chu2aiyaivesa', hash: '$2y$12$RNriKZgyJRr3d3w8/YrpKO46GzwAQ1xgSNgWAzv469o2011TovIN6'],
                kibanaro: [password: 'CaiM2thieZa3bio4', hash: '$2y$12$8dak/kAez6TtKuXKqSmL3u7tTsnOfhyXDgnEOZCNagSy6M9EAvIye'],
                readall: [password: 'aekeixa5Seu3eiSh', hash: '$2y$12$OTLshGBisDznLCEuS8hTE.xSRihxv5JdICL8z0XRtxtByikjbGsEK'],
                snapshotrestore: [password: 'aenaeghiQuei3chu', hash: '$2y$12$mWUXiAuU0on.q1nZ4Mka5e3I4UDgh5t7HX.ufBEqNQLcscypjNUA6'],
            ],
            cookiePass: "Keeth4aidoh0phoh6thie7oF5fiePa5e",
            openidConnectUrl: "https://${keycloakVars.hosts[0]}/auth/realms/public/.well-known/openid-configuration",
            jwtUrl: "",
        ]
    ]
    vars << [
        elasticMaster: [
            limits: [memory: "1000Mi"],
            requests: [memory: "1000Mi"],
            heap: [start: "350m", max: "350m"],
            affinity: [key: "robobeerun.com/elastic-master", name: "required", required: true],
            allowOnMaster: true,
            replicas: 1,
        ]
    ]
    vars << [
        elasticClient: [
            limits: [memory: "1000Mi"],
            requests: [memory: "1000Mi"],
            heap: [start: "350m", max: "350m"],
            affinity: [key: "robobeerun.com/elastic-client", name: "required", required: true],
            allowOnMaster: false,
            replicas: 1,
        ]
    ]
    vars << [
        elasticData: [
            limits: [memory: "1000Mi"],
            requests: [memory: "1000Mi"],
            heap: [start: "350m", max: "350m"],
            affinity: [key: "robobeerun.com/elastic-data", name: "required", required: true],
            allowOnMaster: true,
            replicas: 3,
            storage: [class: "managed-nfs-storage", size: "10Gi"],
        ]
    ]
    vars << [
        kibana: [
            image: [name: "erwinnttdata/kibana-patch", version: "0.9.0-r.1"],
            limits: [memory: "500Mi"],
            requests: [memory: "500Mi"],
            affinity: [key: "robobeerun.com/kibana", name: "required", required: true],
            allowOnMaster: false,
            cookiePass: "Jeid3sungeitoowugh0ievee9quaru6g",
            revision: "r3",
            nginx: [],
            hosts: ["kibana.andrea.muellerpublic.de"],
            issuer: "letsencrypt-prod",
            openid: [
                url: "https://${keycloakVars.hosts[0]}",
                id: "es-kibana",
                secret: "116287c0-48b7-4bcd-9dc3-6bc5892805e8",

            ],
        ]
    ]
    vars << [
        fluentd: [
            image: [name: "fluent/fluentd-kubernetes-daemonset", version: "v1.4.2-debian-elasticsearch-1.0"],
            limits: [memory: "250Mi"],
            requests: [memory: "250Mi"],
            affinity: [key: "robobeerun.com/fluentd", name: "required", required: true],
            allowOnMaster: true,
            revision: "r2",
        ]
    ]
}
''',
            scriptVars: [
                targetHosts: [masters: mastersHosts, nodes: nodesHosts],
                socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey,
                elasticVars: elasticVars, keycloakVars: keycloakVars
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }
}
