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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_10

import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_10_TestUtils.*

import org.apache.commons.io.IOUtils

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFail2banScriptTest extends AbstractFail2banRunnerTest {

    static URL jail2banConf = AbstractFail2banScriptTest.class.getResource("fail2ban_conf.txt")

    static URL jailLocal = AbstractFail2banScriptTest.class.getResource("jail_local.txt")

    void createDummyCommands(File dir) {
        def configDir = new File(dir, 'etc/fail2ban')
        configDir.mkdirs()
        IOUtils.copy jail2banConf.openStream(), new FileOutputStream(new File(configDir, "fail2ban.conf"))
        IOUtils.copy jailLocal.openStream(), new FileOutputStream(new File(configDir, "jail.conf"))
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createIdCommand dir
        createEchoCommands dir, [
            'touch',
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
            'dpkg',
            'curl',
            'sleep',
            'docker',
            'kubectl',
            'ufw',
        ]
    }
}
