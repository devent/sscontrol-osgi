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
package com.anrisoftware.sscontrol.icinga.script.debian.internal.debian_9.icinga_2

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
class IcingaServerTest extends AbstractIcingaRunnerTest {

    @Test
    void "basic_server"() {
        def test = [
            name: "basic_server",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
service "icinga", version: "2"
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            before: { Map test -> },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource IcingaServerTest, readRemoteFile(new File('/etc/apt/sources.list.d', 'icinga.list').absolutePath), "${args.test.name}_icinga_list_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "api_server"() {
        def test = [
            name: "api_server",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
def mysql = [user: "icinga", database: "icinga", password: "icinga"]
service "icinga", version: "2" with {
    plugin 'api'
    config << [name: 'api-users-icingaweb2', script: """\
object ApiUser "icingaweb2" {
  password = "1234" // Change this!
  permissions = [ "actions/*", "objects/modify/hosts", "objects/modify/services", "objects/modify/icingaapplication" ]
}
"""]
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            before: { Map test -> },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                String s = readPrivilegedRemoteFile(new File('/etc/icinga2/conf.d', 'api-users.conf').absolutePath)
                s = s.replaceAll(/password = ".*?"/, 'password = "pass"')
                assertStringResource IcingaServerTest, s, "${args.test.name}_api_users_conf_expected.txt"
                assertStringResource IcingaServerTest, readPrivilegedRemoteFile(new File('/etc/icinga2/conf.d', 'api-users-icingaweb2.conf').absolutePath), "${args.test.name}_api_users_icingaweb2_conf_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "ido_mysql_server"() {
        def test = [
            name: "ido_mysql_server",
            script: '''
service "ssh", host: "robobee@robobee-test", socket: robobeeSocket
def mysql = [user: "icinga", password: "icinga", database: "icinga", adminUser: "icinga-adm", adminPassword: "icinga"]
service "icinga", version: "2" with {
    plugin 'ido-mysql' with {
        database mysql
    }
    feature << [name: 'ido-mysql', script: """\
library "db_ido_mysql"

object IdoMysqlConnection "mysql-ido" {
    host = "127.0.0.1"
    port = 3306
    user = "$mysql.user"
    password = "$mysql.password"
    database = "$mysql.database"

    cleanup = {
        downtimehistory_age = 48h
        contactnotifications_age = 31d
    }
}
"""]
}
''',
            scriptVars: [robobeeSocket: robobeeSocket],
            expectedServicesSize: 2,
            before: { Map test -> },
            after: { Map test -> tearDownServer test: test },
            expected: { Map args ->
                assertStringResource IcingaServerTest, readPrivilegedRemoteFile(new File('/etc/icinga2/features-available', 'ido-mysql.conf').absolutePath), "${args.test.name}_ido_mysql_conf_expected.txt"
            },
        ]
        doTest test
    }

    def tearDownServer(Map args) {
        remoteCommand """
"""
    }

    @Before
    void beforeMethod() {
        assumeTrue new File(robobeeSocket).exists()
    }

    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
        return script
    }
}
