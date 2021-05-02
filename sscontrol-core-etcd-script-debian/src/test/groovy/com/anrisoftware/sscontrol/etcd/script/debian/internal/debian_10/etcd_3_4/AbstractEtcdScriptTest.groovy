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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_10.etcd_3_4

import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_10_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractEtcdScriptTest extends AbstractEtcdRunnerTest {

    static final Map testTlsCerts = [
        ca: AbstractEtcdScriptTest.class.getResource('robobee_test_etcd_ca.pem'),
        cert: AbstractEtcdScriptTest.class.getResource('robobee_test_etcd_etcd_0_server_cert.pem'),
        key: AbstractEtcdScriptTest.class.getResource('robobee_test_etcd_etcd_0_server_key.pem'),
    ]

    static final Map testClientCerts = [
        ca: AbstractEtcdScriptTest.class.getResource('robobee_test_etcd_ca.pem'),
        cert: AbstractEtcdScriptTest.class.getResource('robobee_test_etcd_kube_0_cert.pem'),
        key: AbstractEtcdScriptTest.class.getResource('robobee_test_etcd_kube_0_key.pem'),
    ]

    static final URL grepActiveCommand = AbstractEtcdScriptTest.class.getResource('grep_active_command.txt')

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        new File(dir, '/var/lib/robobee/tmp').mkdirs()
        def d = new File(dir, '/usr/local/share')
        d.mkdirs()
        createFile EtcdScriptTest.class.getResource("etcdctl_vars.txt"), d, 'etcdctl-vars'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'systemctl',
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg',
            'dpkg',
            'ifdown',
            'ifup',
            'source',
            'etcdctl',
        ]
    }
}
