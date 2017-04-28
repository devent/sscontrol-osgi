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
package com.anrisoftware.sscontrol.hostname.debian_8.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class Hostname_Debian_8_Server_Test extends AbstractTestHostname_Debian_8 {

    @Test
    void "hostname script"() {
        def test = [
            name: "hostname_script_fqdn",
            input: """
service "ssh", host: "robobee@robobee-test", key: "$robobeeKey"
service "hostname" with {
    // Sets the hostname.
    set fqdn: "robobee-test.muellerpublic.de"
}
""",
            expected: { Map args ->
                assertStringResource Hostname_Debian_8_Server_Test, readRemoteFile('/etc/hostname'), "${args.test.name}_hostname_expected.txt"
                assertStringResource Hostname_Debian_8_Server_Test, remoteCommand('hostname -f'), "${args.test.name}_hostname_f_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue testHostAvailable
    }

    Map getScriptEnv(Map args) {
        emptyScriptEnv
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
