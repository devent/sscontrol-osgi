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
package com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractHAProxyScriptTest extends AbstractHAProxyRunnerTest {

    static final URL haproxyCfg = AbstractHAProxyScriptTest.class.getResource('haproxy_cfg.txt')
    
    @Override
    void createDummyCommands(File dir) {
        createCommand ufwActiveCommand, dir, 'ufw'
        createCommand catCommand, dir, 'cat'
        createCommand grepCommand, dir, 'grep'
        new File(dir, '/etc/haproxy').mkdirs()
        createFile haproxyCfg, new File(dir, '/etc/haproxy'), 'haproxy.cfg'
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
            'apt-get'
        ]
    }
}
