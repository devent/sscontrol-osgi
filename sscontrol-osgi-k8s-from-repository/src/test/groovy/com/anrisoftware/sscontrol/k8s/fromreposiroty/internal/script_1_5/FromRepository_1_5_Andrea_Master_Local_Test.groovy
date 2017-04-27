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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
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
class FromRepository_1_5_Andrea_Master_Local_Test extends Abstract_1_5_ScriptTest {

    @Test
    void "andrea-local"() {
        def test = [
            name: "andrea-local",
            script: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default' with {
    credentials type: 'cert', name: 'default-admin', ca: cert.ca, cert: cert.cert, key: cert.key
}
service "monitoring-cluster-heapster-influxdb-grafana", cluster: 'default'
""",
            scriptVars: ["certs": andreaLocalCerts],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 3,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeTrue isHostAvailable([
            'andrea-master-local',
            'andrea-node-0-local'
        ])
    }

    void createDummyCommands(File dir) {
    }
}
