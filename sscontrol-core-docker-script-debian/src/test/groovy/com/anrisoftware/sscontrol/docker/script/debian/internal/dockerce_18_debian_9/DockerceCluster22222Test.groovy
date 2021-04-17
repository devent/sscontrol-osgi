/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3Port22222AvailableCondition

import groovy.util.logging.Slf4j


/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@ExtendWith(Nodes3Port22222AvailableCondition.class)
class DockerceCluster22222Test extends AbstractDockerceRunnerTest {

    @Test
    void "cluster_docker"() {
        def test = [
            name: "cluster_docker",
            script: '''
service "ssh" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
service "docker"
''',
            scriptVars: [sockets: nodesSockets],
            expectedServicesSize: 2,
            expected: { Map args ->
                assertStringResource DockerceCluster22222Test, remoteCommand('sudo docker info', 'node-0.robobee-test.test').replaceAll("(?m)^ID: .*", "ID: ID"), "${args.test.name}_docker_info_node_0_expected.txt"
                assertStringResource DockerceCluster22222Test, remoteCommand('sudo docker info', 'node-1.robobee-test.test', 22222).replaceAll("(?m)^ID: .*", "ID: ID"), "${args.test.name}_docker_info_node_1_expected.txt"
                assertStringResource DockerceCluster22222Test, remoteCommand('sudo docker info', 'node-2.robobee-test.test', 22222).replaceAll("(?m)^ID: .*", "ID: ID"), "${args.test.name}_docker_info_node_2_expected.txt"
            },
        ]
        doTest test
    }

    void createDummyCommands(File dir) {
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv(args)
    }
}
