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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.k8s_1_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

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
service "k8s-cluster"
service "repo-git", group: "glusterfs-heketi" with {
    credentials "ssh", key: robobeeKey
    remote url: "git@github.com:robobee-repos/glusterfs-heketi.git"
}
service "glusterfs-heketi", repo: "glusterfs-heketi", name: "glusterfs", nodes: "default" with {
    admin key: "MySecret"
    user key: "MyVolumeSecret"
    storage address: "10.2.35.3:8080"
    property << "gluster_kubernetes_deploy_command=/tmp/gk-deploy"
    property << "kubectl_command=/tmp/kubectl"
    property << "commands_quiet=false"
    property << "node_packages=lvm2:,thin-provisioning-tools:"
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
            scriptVars: [robobeeKey: robobeeKey, robobeeSocket: robobeeSocket, certs: certs],
            expectedServicesSize: 4,
            before: { setupServer it },
            after: { tearDownServer it },
            expected: { Map args ->
                assertStringResource GlusterfsHeketiServerTest, checkRemoteFiles("${args.dir}/usr/local/bin/heketi-cli"), "${args.test.name}_local_bin_heketi_cli_expected.txt"
                assertStringResource GlusterfsHeketiServerTest, readRemoteFile(new File("${args.dir}/opt/tmp", 'gk-deploy.out').absolutePath), "${args.test.name}_gk_deploy_expected.txt"
                assertStringResource GlusterfsHeketiServerTest, readRemoteFile(new File("${args.dir}/tmp", 'kubectl.out').absolutePath), "${args.test.name}_kubectl_expected.txt"
                assertStringResource GlusterfsHeketiServerTest, readRemoteFile(new File("${args.dir}/etc", 'modules').absolutePath), "${args.test.name}_modules_expected.txt"
            },
        ]
        doTest test
    }

    def setupServer(Map args) {
        println args.dir
        remoteCommand """
mkdir -p ${args.dir}/tmp
mkdir -p ${args.dir}/opt/tmp
mkdir -p ${args.dir}/etc/apt/sources.list.d
mkdir -p ${args.dir}/etc

file="${args.dir}/opt/tmp/gk-deploy"
echo "Create \$file"
cat > \$file << 'EOL'
${IOUtils.toString(echoCommand.openStream(), StandardCharsets.UTF_8)}
EOL
chmod +x \$file

file="${args.dir}/tmp/kubectl"
echo "Create \$file"
cat > \$file << 'EOL'
${IOUtils.toString(kubectlCommand.openStream(), StandardCharsets.UTF_8)}
EOL
chmod +x \$file

file="${args.dir}/etc/modules"
echo "Create \$file"
cat > \$file << 'EOL'
# /etc/modules: kernel modules to load at boot time.
#
# This file contains the names of kernel modules that should be loaded
# at boot time, one per line. Lines beginning with "#" are ignored.

EOL

"""
    }

    def tearDownServer(Map args) {
        remoteCommand """
rm -rf ${args.dir}
"""
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
    }

    Map getScriptEnv(Map args) {
        def env = getEmptyScriptEnv args
        env.base = args.dir
        env
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
