/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Test

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.external.GlusterfsHeketi
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.utils.AbstractGlusterfsHeketiRunnerTest
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class GlusterfsHeketiTest extends AbstractGlusterfsHeketiRunnerTest {

    @Inject
    RobobeeScriptFactory robobeeScriptFactory

    @Test
    void "json topology"() {
        def test = [
            name: 'json topology',
            script: '''
service "glusterfs-heketi", cluster: "default", name: "glusterfs" with {
    admin key: "MySecret"
    user key: "MyVolumeSecret"
    topology parse: """
{
  "clusters":[
    {
      "nodes":[
        {
          "node":{
            "hostnames":{
              "manage":[
                "node0"
              ],
              "storage":[
                "192.168.10.100"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        },
        {
          "node":{
            "hostnames":{
              "manage":[
                "node1"
              ],
              "storage":[
                "192.168.10.101"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        },
        {
          "node":{
            "hostnames":{
              "manage":[
                "node2"
              ],
              "storage":[
                "192.168.10.102"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/vdb",
            "/dev/vdc",
            "/dev/vdd"
          ]
        }
      ]
    }
  ]
}
"""
}
''',
            before: { },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('glusterfs-heketi').size() == 1
                GlusterfsHeketi s = services.getServices('glusterfs-heketi')[0]
                assert s.admin.key == 'MySecret'
                assert s.user.key == 'MyVolumeSecret'
                assert s.topology.size() == 1
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        test.before(test)
        def services = servicesFactory.create()
        services.targets.addTarget SshFactory.localhost(injector)
        putServices services
        robobeeScriptFactory.create folder.newFile(), test.script, test.scriptVars, services call()
        Closure expected = test.expected
        expected services
    }

    @Override
    void createDummyCommands(File dir) {
    }
}
