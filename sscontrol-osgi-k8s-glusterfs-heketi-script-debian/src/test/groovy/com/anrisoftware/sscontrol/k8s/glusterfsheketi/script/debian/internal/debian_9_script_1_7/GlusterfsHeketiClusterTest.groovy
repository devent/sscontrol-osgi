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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.debian_9_script_1_7

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
    host "robobee@andrea-master.robobee-test.test", socket: robobeeSocket
    host "robobee@node-1.robobee-test.test", socket: robobeeSocket
}
service "ssh", group: "gluster-node-0" with {
    host "robobee@andrea-master.robobee-test.test", socket: robobeeSocket
}
service "ssh", group: "gluster-node-1" with {
    host "robobee@node-1.robobee-test.test", socket: robobeeSocket
}
service "k8s-cluster" with {
    credentials type: 'cert', name: 'default-admin', cert: certs.worker.cert, key: certs.worker.key
}
service "repo-git", group: "glusterfs-heketi" with {
    credentials "ssh", key: robobeeKey
    remote url: "git@github.com:robobee-repos/glusterfs-heketi.git"
}
def glusterNode = [
    targets['gluster-node-0'][0].hostAddress,
    targets['gluster-node-1'][0].hostAddress,
]
def size_M = 5000
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
service "glusterfs-heketi", repo: "glusterfs-heketi", name: "glusterfs", nodes: "default" with {
    admin key: "MySecret"
    user key: "MyVolumeSecret"
    vars << [gluster: [
            limits: [cpu: '1', memory: '256Mi'],
            requests: [cpu: '1', memory: '256Mi']
    ]]
    vars << [tolerations: [
        [key: 'robobeerun.com/dedicated', effect: 'NoSchedule'],
        [key: 'node.alpha.kubernetes.io/ismaster', effect: 'NoSchedule'],
        [key: 'dedicated', effect: 'NoSchedule'],
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
                "${glusterNode[0]}"
              ],
              "storage":[
                "${glusterNode[0]}"
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
                "${glusterNode[1]}"
              ],
              "storage":[
                "${glusterNode[1]}"
              ]
            },
            "zone":2
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
            scriptVars: [robobeeKey: robobeeKey, robobeeSocket: robobeeSocket, certs: certs],
            expectedServicesSize: 5,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        checkRobobeeSocket()
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
