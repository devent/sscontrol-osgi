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
class CrioScriptTest extends AbstractCrioScriptTest {

    @Test
    void "basic_script"() {
        def test = [
            name: "basic_script",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "crio", version: "1.20"
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource CrioScriptTest, gen, "/etc/modules-load.d/crio.conf", "${args.test.name}_crio_conf_expected.txt"
                assertFileResource CrioScriptTest, gen, "/etc/sysctl.d/99-kubernetes-cri.conf", "${args.test.name}_kubernetes_cri_conf_expected.txt"
                assertFileResource CrioScriptTest, gen, "/etc/crio/crio.conf.d/02-cgroup-manager.conf", "${args.test.name}_cgroup_manager_conf_expected.txt"
                assertFileResource CrioScriptTest, dir, "modprobe.out", "${args.test.name}_modprobe_expected.txt"
                assertFileResource CrioScriptTest, dir, "sysctl.out", "${args.test.name}_sysctl_expected.txt"
                assertFileResource CrioScriptTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
                assertFileResource CrioScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource CrioScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CrioScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource CrioScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource CrioScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource CrioScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource CrioScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource CrioScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        doTest test
    }
}
