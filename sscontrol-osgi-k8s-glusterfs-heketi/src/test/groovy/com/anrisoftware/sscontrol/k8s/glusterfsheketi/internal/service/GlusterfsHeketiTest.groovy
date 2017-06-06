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
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin'
}
service "repo-git", group: "glusterfs-heketi" with {
    remote url: "git@github.com:robobee-repos/glusterfs-heketi.git"
}
service "glusterfs-heketi", cluster: "default", repo: "glusterfs-heketi", name: "glusterfs", nodes: "default" with {
    admin key: "MySecret"
    user key: "MyVolumeSecret"
    vars << [heketi: [snapshot: [limit: 32]]]
    vars << [tolerations: [
        [key: 'robobeerun.com/dedicated', effect: 'NoSchedule'],
        [key: 'node.alpha.kubernetes.io/ismaster', effect: 'NoSchedule'],
    ]]
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
                assert s.cluster.clusterName == 'default'
                assert s.nodes == 'default'
                assert s.repo.repo.group == "glusterfs-heketi"
                assert s.labelName == 'glusterfs'
                assert s.vars.size() == 2
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
