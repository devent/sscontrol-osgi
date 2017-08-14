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
package com.anrisoftware.sscontrol.hostname.script.debian.debian_8.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

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
class Hostname_Debian_8_Test extends AbstractTestHostname_Debian_8 {

    @Test
    void "short"() {
        def test = [
            name: "short",
            input: """
service "ssh", host: "localhost"
service "hostname", fqdn: "blog.muellerpublic.de"
""",
            expected: { Map args ->
                File dir = args.dir
                assertFileResource Hostname_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Hostname_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource Hostname_Debian_8_Test, dir, "hostnamectl.out", "${args.test.name}_hostnamectl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "fqdn"() {
        def test = [
            name: "fqdn",
            input: """
service "ssh", host: "localhost"
service "hostname" with {
    // Sets the hostname.
    set fqdn: "blog.muellerpublic.de"
}
""",
            expected: { Map args ->
                File dir = args.dir
                assertFileResource Hostname_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource Hostname_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource Hostname_Debian_8_Test, dir, "hostnamectl.out", "${args.test.name}_hostnamectl_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }
}
