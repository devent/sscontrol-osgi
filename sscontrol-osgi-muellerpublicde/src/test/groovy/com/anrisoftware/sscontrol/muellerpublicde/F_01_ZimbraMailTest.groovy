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
package com.anrisoftware.sscontrol.muellerpublicde

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class F_01_ZimbraMailTest extends Abstract_Runner_Debian_Test {

    @Test
    void "zimbra-mail"() {
        def test = [
            name: "zimbra-mail",
            script: '''
service "ssh" with {
    host "robobee@andrea-mail-0.muellerpublic.de", socket: socketFiles.mails[0]
}
service "hosts" with {
    ip '46.19.34.182', host: 'andrea-mail-0.muellerpublic.de', alias: 'andrea-mail-0'
}
service "zimbra", version: "8.7" with {
    domain email: "erwin.mueller82@gmail.com"
    property << "auto_update_zimbra=false"
}
service "collectd", version: "5.7" with {
    collectdConfigs.each { config it }
}
''',
            scriptVars: [
                socketFiles: socketFiles,
                collectdConfigs: [
                    [name: '10-plugins', script: IOUtils.toString(getClass().getResourceAsStream('collectd_plugins_conf.txt'), "UTF-8")],
                    [name: '99-write-graphite', script: IOUtils.toString(getClass().getResourceAsStream('collectd_graphite_conf.txt'), "UTF-8")],
                ],
            ],
            expectedServicesSize: 4,
            expected: { Map args ->
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        assumeMailExists()
        //assumeTrue "Check hosts: $mastersHosts", isHostAvailable(mastersHosts)
        //assumeTrue "Check hosts: $nodesHosts", isHostAvailable(nodesHosts)
    }
}
