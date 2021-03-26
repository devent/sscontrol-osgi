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
package com.anrisoftware.sscontrol.nfs.script.centos.internal.centos_7.internal

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
class NfsScriptTest extends AbstractNfsScriptTest {

    @Test
    void "nfs_script_exports"() {
        def test = [
            name: "nfs_script_exports",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "nfs", version: "1.3" with {
    export dir: "/nfsfileshare/0" with {
        host << "andrea-node-0.muellerpublic.de"
        host name: "andrea-node-1.muellerpublic.de", options: "rw,sync,no_root_squash"
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource NfsScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource NfsScriptTest, dir, "yum.out", "${args.test.name}_yum_expected.txt"
                assertFileResource NfsScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource NfsScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource NfsScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource NfsScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource NfsScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource NfsScriptTest, dir, "firewall-cmd.out", "${args.test.name}_firewall_cmd_expected.txt"
                assertFileResource NfsScriptTest, gen, "/etc/exports", "${args.test.name}_exports_expected.txt"
                //assertFileResource CollectdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
            },
        ]
        doTest test
    }
}
