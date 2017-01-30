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
package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Fail2ban_0_8_Debian_8_Server_Test extends AbstractTestFail2ban_0_8_Debian_8 {

    @Test
    void "fail2ban script"() {
        if (!testHostAvailable) {
            return
        }
        def test = [
            name: "fail2ban_script_ssh_jail",
            input: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "fail2ban" with {
    debug "debug", level: 4
    banning time: "PT1M"
    jail "ssh"
}
""",
            expected: { Map args ->
                assertStringResource Fail2ban_0_8_Debian_8_Server_Test, readRemoteFile('/etc/fail2ban/fail2ban.local'), "${args.test.name}_fail2ban_local_expected.txt"
                assertStringResource Fail2ban_0_8_Debian_8_Server_Test, readRemoteFile('/etc/fail2ban/jail.local'), "${args.test.name}_jail_local_expected.txt"
                assertStringResource Fail2ban_0_8_Debian_8_Server_Test, readRemoteFile('/etc/fail2ban/action.d/ufw.conf'), "${args.test.name}_ufw_conf_expected.txt"
            },
        ]
        doTest test
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }

    void createDummyCommands(File dir) {
    }
}
