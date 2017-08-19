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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Fail2banScriptTest extends AbstractFail2banScriptTest {

    @Test
    void "script_basic"() {
        def test = [
            name: "script_basic",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "fail2ban"
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            before: { Map test ->
                def configDir = new File(test.dir, 'etc/fail2ban')
                configDir.mkdirs()
                IOUtils.copy jail2banConf.openStream(), new FileOutputStream(new File(configDir, "fail2ban.conf"))
                IOUtils.copy jailLocal.openStream(), new FileOutputStream(new File(configDir, "jail.conf"))
                IOUtils.copy jail2banConf.openStream(), new FileOutputStream(new File(configDir, "fail2ban.local"))
                IOUtils.copy jailLocal.openStream(), new FileOutputStream(new File(configDir, "jail.local"))
            },
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource Fail2banScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Fail2banScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                //assertFileResource Fail2banScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
            }
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
