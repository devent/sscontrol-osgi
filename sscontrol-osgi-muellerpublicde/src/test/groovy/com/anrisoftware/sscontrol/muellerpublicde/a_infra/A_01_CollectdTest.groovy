/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.muellerpublicde.a_infra

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class A_01_CollectdTest extends Abstract_Runner_Debian_Test {

    @Test
    void "collectd on nodes"() {
	def test = [
	    name: "collectd on nodes",
	    script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
    host "robobee@andrea-node-1.muellerpublic.de", socket: socketFiles.nodes[0]
    host "robobee@andrea-node-2.muellerpublic.de", socket: socketFiles.nodes[1]
}
service "collectd", version: "5.7" with {
    config name: "99-write-influxdb", script: """
LoadPlugin "network"
<Plugin "network">
  Server "andrea-node-0.muellerpublic.de" "30826"
</Plugin>
"""
}
''',
	    scriptVars: [socketFiles: socketFiles, robobeeKey: robobeeKey],
	    expectedServicesSize: 2,
	    expected: { Map args ->
	    },
	]
	doTest test
    }

    @Before
    void checkProfile() {
	assumeMastersExists()
	assumeNodesExists()
    }
}
