/*-
 * #%L
 * sscontrol-osgi - hostname-script-debian
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.hostname.script.debian.debian_9.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class HostnameScriptTest extends AbstractTestHostname {

    @Test
    void "short"() {
        def test = [
            name: "short",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "hostname", fqdn: "blog.muellerpublic.de"
''',
            scriptVars: [localhostSocket: localhostSocket],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource HostnameScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource HostnameScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource HostnameScriptTest, dir, "hostnamectl.out", "${args.test.name}_hostnamectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "fqdn"() {
        def test = [
            name: "fqdn",
            input: '''
service "ssh", host: "localhost", socket: localhostSocket
service "hostname" with {
    // Sets the hostname.
    set fqdn: "blog.muellerpublic.de"
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource HostnameScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource HostnameScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource HostnameScriptTest, dir, "hostnamectl.out", "${args.test.name}_hostnamectl_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
