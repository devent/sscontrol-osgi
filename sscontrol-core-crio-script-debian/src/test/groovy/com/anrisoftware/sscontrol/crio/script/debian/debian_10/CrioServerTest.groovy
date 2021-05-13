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
package com.anrisoftware.sscontrol.crio.script.debian.debian_10

import static com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.RobobeeSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(RobobeeSocketCondition.class)
class CrioServerTest extends AbstractCrioRunnerTest {

    @Test
    void "server_basic"() {
        def test = [
            name: "server_basic",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "crio", version: "1.20"
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                assertStringResource CrioServerTest, readRemoteFile('/etc/modules-load.d/crio.conf'), "${args.test.name}_crio_conf_expected.txt"
                assertStringResource CrioServerTest, readRemoteFile('/etc/sysctl.d/99-kubernetes-cri.conf'), "${args.test.name}_99_kubernetes_cri_conf_expected.txt"
                assertStringResource CrioServerTest, readRemoteFile('/etc/apt/sources.list.d/crio.list'), "${args.test.name}_crio_list_expected.txt"
                assertStringResource CrioServerTest, readRemoteFile('/etc/apt/sources.list.d/libcontainers.list'), "${args.test.name}_libcontainers_list_expected.txt"
            },
        ]
        doTest test
    }

    @Override
    void createDummyCommands(File dir) {
    }

    @Override
    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
