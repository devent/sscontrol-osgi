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
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class DockerceClusterTest extends AbstractDockerceRunnerTest {

    static final URL robobeeKey = UnixTestUtil.class.getResource('robobee')

    static final Map certs = [
        ca: DockerceClusterTest.class.getResource('registry_robobee_test_test_ca.pem'),
    ]

    @Test
    void "cluster_docker"() {
        def test = [
            name: "cluster_docker",
            script: '''
service "ssh" with {
    host "robobee@robobee-test", socket: "/tmp/robobee@robobee-test:22"
    host "robobee@robobee-1-test", socket: "/tmp/robobee@robobee-1-test:22"
}
service "docker" with {
    registry mirror: 'https://registry.robobee-test.test:5000', ca: certs.ca
}
''',
            scriptVars: [certs: certs],
            expectedServicesSize: 2,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue new File('/tmp/robobee@robobee-test:22').exists()
        assumeTrue new File('/tmp/robobee@robobee-1-test:22').exists()
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
