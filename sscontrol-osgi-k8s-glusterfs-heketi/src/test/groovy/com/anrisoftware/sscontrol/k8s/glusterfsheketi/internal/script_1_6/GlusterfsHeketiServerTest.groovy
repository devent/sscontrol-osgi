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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.utils.AbstractGlusterfsHeketiRunnerTest
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.jcabi.ssh.SSH

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class GlusterfsHeketiServerTest extends AbstractGlusterfsHeketiRunnerTest {

    @Test
    void "json_topology_server"() {
        def test = [
            name: "json_topology_server",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', cert: certs.worker.cert, key: certs.worker.key
}
service "repo-git", group: "glusterfs-heketi" with {
    credentials "ssh", key: robobeeKey
    remote url: "git@github.com:robobee-repos/glusterfs-heketi.git"
}
service "glusterfs-heketi", cluster: "default", repo: "glusterfs-heketi", name: "glusterfs" with {
    admin key: "MySecret"
    user key: "MyVolumeSecret"
    property << "kubectl_cmd=/tmp/kubectl"
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
            scriptVars: [robobeeKey: robobeeKey, robobeeSocket: robobeeSocket, certs: andreaLocalCerts],
            expectedServicesSize: 4,
            before: { setupServer test: it },
            after: { tearDownServer test: it },
            expected: { Map args ->
                assertStringResource GlusterfsHeketiServerTest, readRemoteFile(new File('/tmp', 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    def setupServer(Map args) {
        def file = SSH.escape('wordpress-app.zip')
        remoteCommand """
echo "Create /tmp/kubectl."
cat > /tmp/kubectl << 'EOL'
${IOUtils.toString(echoCommand.openStream(), StandardCharsets.UTF_8)}
EOL
chmod +x /tmp/kubectl
"""
    }

    def tearDownServer(Map args) {
        remoteCommand """
rm /tmp/kubectl
rm /tmp/kubectl.out
"""
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
        assumeTrue testHostAvailable
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
