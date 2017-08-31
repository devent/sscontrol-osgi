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
package com.anrisoftware.sscontrol.icinga.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.icinga.service.external.Icinga
import com.anrisoftware.sscontrol.icinga.service.internal.IcingaImpl.IcingaImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class IcingaScriptTest {

    @Inject
    IcingaImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "service"() {
        def test = [
            name: 'service',
            input: """
service "icinga", version: "2"
""",
            expected: { HostServices services ->
                assert services.getServices('icinga').size() == 1
                Icinga s = services.getServices('icinga')[0]
                assert s.name == 'icinga-2'
            },
        ]
        doTest test
    }

    @Test
    void "feature"() {
        def test = [
            name: 'feature',
            input: '''
def mysql = [user: "icinga", database: "icinga", password: "icinga"]
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
            expected: { HostServices services ->
                assert services.getServices('icinga').size() == 1
                Icinga s = services.getServices('icinga')[0]
                assert s.plugins.size() == 1
                assert s.plugins[0].name == "ido-mysql"
                assert s.plugins[0].database.user == "icinga"
                assert s.plugins[0].database.password == "icinga"
                assert s.plugins[0].database.database == "icinga"
                assert s.features.size() == 1
                assert s.features[0] =~ /.*library.*/
                assert s.features[0] =~ /.*password = "icinga".*/
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: { 'default' }, getHosts: { []}] as Ssh)
        services.putAvailableService 'icinga', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new IcingaModule(),
                new PropertiesModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
