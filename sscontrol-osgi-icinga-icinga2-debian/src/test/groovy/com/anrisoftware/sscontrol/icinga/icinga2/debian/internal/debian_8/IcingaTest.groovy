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
package com.anrisoftware.sscontrol.icinga.icinga2.debian.internal.debian_8

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
class IcingaTest extends AbstractIcingaScriptTest {

    @Test
    void "basic_script"() {
        def test = [
            name: "basic_script",
            input: """
service "ssh", host: "localhost"
service "icinga", version: "2"
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource IcingaTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource IcingaTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "api_script"() {
        def test = [
            name: "api_script",
            input: '''
service "ssh", host: "localhost"
service "icinga", version: "2" with {
    plugin 'api'
    config << [name: 'api-users', script: """
object ApiUser "web2" {
  password = "bea11beb7b810ea9ce6ea" // Change this!
  permissions = [ "actions/*", "objects/modify/hosts", "objects/modify/services", "objects/modify/icingaapplication" ]
}
"""]
}
''',
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource IcingaTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource IcingaTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource IcingaTest, dir, "icinga2.out", "${args.test.name}_icinga2_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "ido_mysql_script"() {
        def test = [
            name: "ido_mysql_script",
            input: """
service "ssh", host: "localhost"
def mysql = [user: "icinga", database: "icinga", password: "icinga"]
service "icinga", version: "2" with {
    plugin 'ido-mysql' with {
        database mysql
    }
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource IcingaTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource IcingaTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }
}
