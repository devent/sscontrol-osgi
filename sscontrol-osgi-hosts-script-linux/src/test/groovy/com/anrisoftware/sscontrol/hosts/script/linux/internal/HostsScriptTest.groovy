/*-
 * #%L
 * sscontrol-osgi - hosts-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.hosts.script.linux.internal

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
class HostsScriptTest extends AbstractTestHosts {

    @Test
    void "explicit_list"() {
        def test = [
            name: "explicit_list",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "hosts" with {
    ip "192.168.0.52", host: "srv1.ubuntutest.com"
    ip "192.168.0.49", host: "srv1.ubuntutest.de", alias: "srv1"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource HostsScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource HostsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HostsScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource HostsScriptTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
                assertFileResource HostsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HostsScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
            },
        ]
        doTest test
    }
}
