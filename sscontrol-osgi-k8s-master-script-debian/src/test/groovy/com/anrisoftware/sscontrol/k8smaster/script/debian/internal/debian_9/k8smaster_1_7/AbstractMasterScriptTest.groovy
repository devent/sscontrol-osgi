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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.debian_9.k8smaster_1_7

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractMasterScriptTest extends AbstractMasterRunnerTest {

    static final Map certs = [
        ca: AbstractMasterScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractMasterScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractMasterScriptTest.class.getResource('cert_key.txt'),
    ]

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        createIdCommand dir
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
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'curl',
            'sleep',
            'kubectl',
            'ufw',
        ]
    }
}
