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
package com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

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
class MonitoringClusterHeapsterInfluxdbGrafana_1_5_ScriptTest extends Abstract_1_5_ScriptTest {

    @Test
    void "script_unsecured"() {
        def test = [
            name: "script_unsecured",
            script: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default'
service "monitoring-cluster-heapster-influxdb-grafana", cluster: 'default'
""",
            generatedDir: folder.newFolder(),
            expectedServicesSize: 3,
            expected: { Map args ->
                File dir = args.dir
                File binDir = new File(dir, '/usr/local/bin')
                assertFileResource MonitoringClusterHeapsterInfluxdbGrafana_1_5_ScriptTest, binDir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_secured_client_cert"() {
        def test = [
            name: "script_secured_client_cert",
            input: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default' with {
    credentials type: 'cert', name: 'default-admin', ca: '$certCaPem', cert: '$certCertPem', key: '$certKeyPem'
}
service "monitoring-cluster-heapster-influxdb-grafana", cluster: 'default'
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                File binDir = new File(dir, '/usr/local/bin')
                assertFileResource MonitoringClusterHeapsterInfluxdbGrafana_1_5_ScriptTest, binDir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }

    void createDummyCommands(File dir) {
        createDebianJessieCatCommand dir, 'cat'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service',
            'id',
            'basename',
            'wget',
            'which',
            'sha256sum',
        ]
        def binDir = new File(dir, '/usr/local/bin')
        binDir.mkdirs()
        createCommand kubectlCommand, binDir, 'kubectl'
    }
}
