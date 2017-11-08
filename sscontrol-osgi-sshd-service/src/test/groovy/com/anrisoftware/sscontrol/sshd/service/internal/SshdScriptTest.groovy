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
package com.anrisoftware.sscontrol.sshd.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.sshd.service.external.Sshd
import com.anrisoftware.sscontrol.sshd.service.internal.SshdImpl.SshdImplFactory
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
class SshdScriptTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    SshdImplFactory serviceFactory

    @Test
    void "service"() {
        def test = [
            name: 'service',
            input: """
service "sshd"
""",
            expected: { HostServices services ->
                assert services.getServices('sshd').size() == 1
                Sshd hosts = services.getServices('sshd')[0] as Sshd
            },
        ]
        doTest test
    }

    @Test
    void "user"() {
        def test = [
            name: 'user',
            input: """
service "sshd" with {
    user << 'devent'
}
""",
            expected: { HostServices services ->
                assert services.getServices('sshd').size() == 1
                Sshd hosts = services.getServices('sshd')[0] as Sshd
                assert hosts.users.size() == 1
                assert hosts.users.containsAll(["devent"])
            },
        ]
        doTest test
    }

    @Test
    void "debug_level"() {
        def test = [
            name: 'debug_level',
            input: """
service "sshd" with {
    debug level: 1
}
""",
            expected: { HostServices services ->
                assert services.getServices('sshd').size() == 1
                Sshd hosts = services.getServices('sshd')[0] as Sshd
            },
        ]
        doTest test
    }

    @Test
    void "binding"() {
        def test = [
            name: 'binding',
            input: """
service "sshd" with {
    bind port: 2222
}
""",
            expected: { HostServices services ->
                assert services.getServices('sshd').size() == 1
                Sshd hosts = services.getServices('sshd')[0] as Sshd
                assert hosts.binding.port == 2222
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'sshd', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new SshdModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new DebugLoggingModule(),
                new StringsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
