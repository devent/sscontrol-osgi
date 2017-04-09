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

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class MonitoringClusterHeapsterInfluxdbGrafana_1_5_Test extends Abstract_1_5_Test {

    @Test
    void "unsecured"() {
        def test = [
            name: "unsecured",
            input: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default', group: 'default'
service "monitoring-cluster-heapster-influxdb-grafana", cluster: 'default'
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                File binDir = new File(dir, '/usr/local/bin')
                assertFileResource MonitoringClusterHeapsterInfluxdbGrafana_1_5_Test, binDir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }
}
