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
package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFlannelDockerScriptTest extends AbstractFlannelDockerRunnerTest {

    static final Map testCerts = [
        ca: AbstractFlannelDockerScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractFlannelDockerScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractFlannelDockerScriptTest.class.getResource('cert_key.txt'),
    ]

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        cert: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_client_0_robobee_test_cert.pem'),
        key: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_client_0_robobee_test_key_insecure.pem'),
    ]

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service',
            'systemctl',
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg',
            'curl',
            'mktemp',
            'docker',
        ]
    }
}
