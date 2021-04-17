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
package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.centos.external.CentosTestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractZimbraScriptTest extends AbstractZimbraRunnerTest {

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, 'cat'
        createCommand yumCommand, dir, 'yum'
        createCommand AbstractZimbraScriptTest.class.getResource('wc_custom_command.txt'), dir, 'wc'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'systemctl',
            'which',
            'id',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'grep',
            'gpg',
            'sha256sum',
            'expect',
            'firewall-cmd',
            'docker',
        ]
    }
}
