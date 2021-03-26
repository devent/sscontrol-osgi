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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_13.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractNodeScriptTest extends AbstractNodeRunnerTest {

    static final Map certs = [
        ca: AbstractNodeScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractNodeScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractNodeScriptTest.class.getResource('cert_key.txt'),
    ]

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        new File(dir, "/etc/apt/sources.list.d").mkdirs()
        createEchoCommands dir, [
            'id',
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'dpkg',
            'systemctl',
            'which',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'curl',
            'sleep',
            'docker',
            'kubectl',
            'ufw',
            'modprobe',
            'kubeadm',
        ]
    }
}
