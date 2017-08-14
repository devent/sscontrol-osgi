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
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractDockerceScriptTest extends AbstractDockerceDebianRunnerTest {

    static final URL certCaPem = AbstractDockerceScriptTest.class.getResource('cert_ca.txt')

    static final URL muellerpublicCertCaPem = AbstractDockerceScriptTest.class.getResource('muellerpublic_de_ca_cert.pem')

    void createDummyCommands(File dir) {
        createCommand exit1Command, dir, 'dpkg'
        createCommand exit1Command, dir, 'grep'
        createEchoCommands dir, [
            'cat',
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
            'update-grub',
        ]
    }
}
