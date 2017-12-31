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
package com.anrisoftware.sscontrol.dhclient.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.dhclient.service.external.Dhclient
import com.anrisoftware.sscontrol.dhclient.service.internal.DhclientImpl.DhclientImplFactory
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
class DhclientScriptTest {

    @Inject
    DhclientImplFactory dhclientFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "dhclient service script"() {
        def test = [
            input: """
service "dhclient" with {
    option 'rfc3442-classless-static-routes code 121 = array of unsigned integer 8'
    send 'host-name', 'andare.fugue.com';
    send 'host-name = gethostname()'
    request '!domain-name-servers'
    prepend 'domain-name-servers', '127.0.0.1'
    declare 'interface', 'eth0' with {
        // interface eth0
    }
    declare 'alias' with {
        // alias
    }
    declare 'lease' with {
        // lease
    }
}
""",
            expected: { HostServices services ->
                assert services.getServices('dhclient').size() == 1
                Dhclient s = services.getServices('dhclient')[0]
                assert s.statements.size() == 8
            },
        ]
        doTest test
    }

    @Test
    void "dhclient service properties"() {
        def test = [
            input: """
service "dhclient" with {
    property << 'default_option = rfc3442-classless-static-routes code 121 = array of unsigned integer 8'
    property << 'default_sends = host-name "<hostname>"'
    property << 'default_sends = host-name "my.hostname"'
}
""",
            expected: { HostServices services ->
                assert services.getServices('dhclient').size() == 1
                Dhclient s = services.getServices('dhclient')[0]
                assert s.statements.size() == 0
                assert s.serviceProperties.propertyNames.size() == 2
                assert s.serviceProperties.getProperty('default_sends') == 'host-name "my.hostname"'
                assert s.serviceProperties.propertyNames.containsAll([
                    'default_option',
                    'default_sends'
                ])
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'dhclient', dhclientFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new DhclientModule(),
                new PropertiesModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                new StringsModule(),
                ).injectMembers(this)
    }
}
