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
class GlusterfsHeketiClusterTest extends AbstractGlusterfsHeketiRunnerTest {

    @Test
    void "json_topology_server"() {
        def test = [
            name: "json_topology_server",
            script: '''
service "ssh" with {
    host "robobee@node-0.robobee-test.test", socket: sockets.nodes[0]
    host "robobee@node-1.robobee-test.test", socket: sockets.nodes[1]
    host "robobee@node-2.robobee-test.test", socket: sockets.nodes[2]
}
service "ssh", group: "masters" with {
    host "robobee@andrea-master.robobee-test.test", socket: sockets.masters[0]
}
service "k8s-cluster", target: 'masters' with {
}
service "repo-git", group: "glusterfs-heketi", target: targets.default[0] with {
    credentials "ssh", key: robobeeKey
    remote url: "git@github.com:robobee-repos/glusterfs-heketi.git"
}
service "shell", privileged: true with {
    script << """
set -e
cat <<EOF > /usr/local/bin/create-glusterfs-lo.sh
#!/bin/bash
set -e
/bin/mkdir -p /disks
if [ ! -f /disks/disk-0 ]; then
  /bin/dd if=/dev/zero of=/disks/disk-0 bs=1M count=${size_M}
fi
EOF

chmod +x /usr/local/bin/create-glusterfs-lo.sh

cat <<EOF > /etc/systemd/system/glusterfs-lo.service
[Unit]
Description=Setup loop devices for glusterfs.
After=local-fs.target
Before=kubelet.service

[Service]
Type=oneshot
ExecStartPre=/usr/local/bin/create-glusterfs-lo.sh
ExecStart=/sbin/losetup /dev/loop10 /disks/disk-0
ExecStop=/sbin/losetup -D /dev/loop10
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
EOF
"""
    script timeout: "PT2H", command: """
set +e
systemctl daemon-reload
systemctl start glusterfs-lo
systemctl enable glusterfs-lo
"""
}
def glusterNodesHosts = [
    "node-0",
    "node-1",
    "node-2",
]
def glusterNodesAddresses = targets.default.inject([]) { list, host ->
    list << host.hostAddress
}
service "glusterfs-heketi", target: "masters", repo: "glusterfs-heketi", name: "glusterfs", nodes: "default" with {
    property << "commands_quiet=false"
    property << "debug_gk_deploy=true"
    admin key: "cai7jie5vooqu8uF"
    user key: "Aif1reigei2ooth4"
    topology parse: """
{
  "clusters":[
    {
      "nodes":[
        {
          "node":{
            "hostnames":{
              "manage":[
                "${glusterNodesHosts[0]}"
              ],
              "storage":[
                "${glusterNodesAddresses[0]}"
              ]
            },
            "zone":1
          },
          "devices":[
            "/dev/loop10"
          ]
        },
        {
          "node":{
            "hostnames":{
              "manage":[
                "${glusterNodesHosts[1]}"
              ],
              "storage":[
                "${glusterNodesAddresses[1]}"
              ]
            },
            "zone":2
          },
          "devices":[
            "/dev/loop10"
          ]
        },
        {
          "node":{
            "hostnames":{
              "manage":[
                "${glusterNodesHosts[2]}"
              ],
              "storage":[
                "${glusterNodesAddresses[2]}"
              ]
            },
            "zone":3
          },
          "devices":[
            "/dev/loop10"
          ]
        }
      ]
    }
  ]
}
"""
}
''',
            scriptVars: [
                sockets: sockets, size_M: "20000", robobeeKey: robobeeKey,
            ],
            expectedServicesSize: 5,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    static final Map sockets = [
        masters: [
            "/tmp/robobee@robobee-test:22"
        ],
        nodes: [
            "/tmp/robobee@robobee-test:22",
            "/tmp/robobee@robobee-1-test:22",
            "/tmp/robobee@robobee-2-test:22",
        ],
    ]

    @Before
    void beforeMethod() {
        assumeSocketsExists sockets.nodes
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
