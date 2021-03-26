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
import static com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@EnabledIfSystemProperty(named = 'project.custom.local.tests.enabled', matches = 'true')
@ExtendWith(LocalhostSocketCondition.class)
class ZimbraScriptTest extends AbstractZimbraScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "zimbra", version: "8.7"
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ZimbraScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "yum.out", "${args.test.name}_yum_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_lets_encrypt"() {
        def test = [
            name: "script_lets_encrypt",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "zimbra", version: "8.7" with {
    domain email: "erwin.mueller82@gmail.com"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            before: { Map test ->
                File dir = test.dir
                new File(dir, '/sbin').mkdirs()
            },
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ZimbraScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "yum.out", "${args.test.name}_yum_expected.txt"
                assertFileResource ZimbraScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
            },
        ]
        doTest test
    }
}
