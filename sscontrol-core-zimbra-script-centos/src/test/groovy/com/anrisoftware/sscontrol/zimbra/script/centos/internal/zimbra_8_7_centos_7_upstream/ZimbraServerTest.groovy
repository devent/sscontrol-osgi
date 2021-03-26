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
package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream.ZimbraSocketCondition.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(ZimbraSocketCondition.class)
class ZimbraServerTest extends AbstractZimbraRunnerTest {

    @Test
    void "zimbra_server"() {
        def test = [
            name: "zimbra_server",
            script: """
service "ssh", host: "robobee@mail.robobee.test", socket: "$zimbraSocket"
service "zimbra", version: "8.7"
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                //assertStringResource ZimbraServerTest, checkRemoteFiles('/opt/zimbra/*'), "${args.test.name}_zimbra_dir_expected.txt"
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
