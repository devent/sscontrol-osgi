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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.etcd_3_2

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractEtcdScriptTest extends AbstractEtcdRunnerTest {

    static final Map testCerts = [
        ca: AbstractEtcdScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractEtcdScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractEtcdScriptTest.class.getResource('cert_key.txt'),
    ]

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        etcd_0_cert: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_0_robobee_test_cert.pem'),
        etcd_0_key: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_0_robobee_test_key_insecure.pem'),
        etcd_1_cert: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_1_robobee_test_cert.pem'),
        etcd_1_key: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_1_robobee_test_key_insecure.pem'),
    ]

    static final URL grepActiveCommand = AbstractEtcdScriptTest.class.getResource('grep_active_command.txt')

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
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
            'which',
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg',
            'dpkg'
        ]
    }
}