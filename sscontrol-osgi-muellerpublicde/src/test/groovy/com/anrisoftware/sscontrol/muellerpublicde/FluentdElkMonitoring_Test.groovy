/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
class FluentdElkMonitoring_Test extends Abstract_Runner_Debian_Test {

    @Test
    void "andrea-mueller use host"() {
        def test = [
            name: "andrea-mueller use host",
            script: """
service "ssh", socket: "${socketFiles.masters[0]}" with {
    host "robobee@andrea-master-0.muellerpublic.de"
}
service "ssh", group: "masters", socket: "${socketFiles.masters[0]}" with {
    host "robobee@andrea-master-0.muellerpublic.de"
}
service "k8s-cluster", target: 'masters' with {
    credentials type: 'cert', name: 'robobee-admin', ca: k8s_vars.admin.certs.ca, cert: k8s_vars.admin.certs.cert, key: k8s_vars.admin.certs.key
}
service "git", group: "fluentd-elk-monitoring" with {
    remote url: "git@github.com:robobee-repos/fluentd-elk-monitoring.git"
    credentials "ssh", key: "$robobeeKey"
}
service "from-repository", repo: "fluentd-elk-monitoring" with {
    vars << [
        elasticsearch: [
            image: [name: 'docker.elastic.co/elasticsearch/elasticsearch', version: '5.4.0'],
            cluster: [name: 'elasticsearch-logging'],
            master_nodes: 1,
            memory_mb: '512'
        ],
        fluentd: [
            image: [name: 'docker.io/erwinnttdata/fluentd-elk-debian', version: '1.14.14-1.0.0'],
        ],
        kibana: [
            image: [name: 'gcr.io/google_containers/kibana', version: 'v4.6.1-1'],
        ],
    ]
}
""",
            scriptVars: [etcd_vars: etcd_vars, k8s_vars: k8s_vars],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeTrue new File(socketFiles.masters[0]).exists()
        //assumeTrue isHostAvailable([
        //    'andrea-master-0.muellerpublic.de',
        //    'andrea-node-0.muellerpublic.de'
        //])
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
