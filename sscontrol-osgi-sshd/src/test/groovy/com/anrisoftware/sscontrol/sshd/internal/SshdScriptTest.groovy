/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.sshd.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.sshd.external.Sshd
import com.anrisoftware.sscontrol.sshd.internal.SshdImpl.SshdImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.google.inject.AbstractModule
import com.google.inject.Guice

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@CompileStatic
class SshdScriptTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    SshdImplFactory sshdFactory

    @Test
    void "sshd service"() {
        def testCases = [
            [
                input: """
service "sshd"
""",
                expected: { HostServices services ->
                    assert services.getServices('sshd').size() == 1
                    Sshd hosts = services.getServices('sshd')[0] as Sshd
                },
            ],
            [
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
            ],
            [
                input: """
service "sshd" with {
    debug level: 1
}
""",
                expected: { HostServices services ->
                    assert services.getServices('sshd').size() == 1
                    Sshd hosts = services.getServices('sshd')[0] as Sshd
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '\n#### {}. case: {}', k, test
            def services = servicesFactory.create()
            services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
            services.putAvailableService 'sshd', sshdFactory
            Eval.me 'service', services, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new SshdModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new DebugLoggingModule(),
                new StringsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
