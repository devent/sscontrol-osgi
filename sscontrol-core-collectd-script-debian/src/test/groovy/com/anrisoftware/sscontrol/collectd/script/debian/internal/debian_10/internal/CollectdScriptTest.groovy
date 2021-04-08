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
package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_10.internal

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
@EnabledIfSystemProperty(named = "project.custom.local.tests.enabled", matches = "true")
@ExtendWith(LocalhostSocketCondition.class)
class CollectdScriptTest extends AbstractCollectdScriptTest {

    @Test
    void "collectd_script"() {
        def test = [
            name: "collectd_script",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "collectd", version: "5.8" with {
    config name: "99-write-graphite", script: """
LoadPlugin "write_graphite"
<Plugin "write_graphite">
<Node "graphite">
  Host "graphite"
  Port "2003"
  Prefix "collectd."
  #Postfix ""
  Protocol "tcp"
  LogSendErrors true
  EscapeCharacter "_"
  SeparateInstances true
  StoreRates true
  AlwaysAppendDS false
</Node>
</Plugin>
"""
    config name: "99-write-influxdb", script: """
LoadPlugin "network"
<Plugin "network">
  Server "192.168.56.1" "25826"
</Plugin>
"""
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource CollectdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CollectdScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource CollectdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CollectdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                //assertFileResource CollectdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
            },
        ]
        doTest test
    }
}
