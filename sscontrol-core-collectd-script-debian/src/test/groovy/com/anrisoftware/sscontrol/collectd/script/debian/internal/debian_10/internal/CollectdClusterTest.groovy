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

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import com.anrisoftware.sscontrol.shell.external.utils.Nodes3AvailableCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@ExtendWith(Nodes3AvailableCondition.class)
class CollectdClusterTest extends AbstractCollectdRunnerTest {

    @Test
    void "collectd_cluster"() {
        def test = [
            name: "collectd_cluster",
            script: '''
service "ssh" with {
    host "robobee@robobee-test", socket: socketFiles[0]
    host "robobee@robobee-1-test", socket: socketFiles[1]
    host "robobee@robobee-2-test", socket: socketFiles[2]
}
service "collectd", version: "5.8" with {
    config name: "99-write-graphite", script: """\
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
    config name: "99-write-influxdb", script: """\
LoadPlugin "network"
<Plugin "network">
  Server "192.168.56.1" "25826"
</Plugin>
"""
    config name: "80-extra-plugins", script: """\
LoadPlugin "uptime"
"""
}
''',
            scriptVars: [socketFiles: [
                "/tmp/robobee@robobee-test:22",
                "/tmp/robobee@robobee-1-test:22",
                "/tmp/robobee@robobee-2-test:22"
                ]],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
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
