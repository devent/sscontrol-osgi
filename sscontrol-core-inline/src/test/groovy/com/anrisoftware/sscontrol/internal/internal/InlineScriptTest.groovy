/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.internal.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger

import com.anrisoftware.globalpom.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.inline.external.Inline
import com.anrisoftware.sscontrol.inline.internal.InlineModule
import com.anrisoftware.sscontrol.inline.internal.InlineImpl.InlineImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.anrisoftware.sscontrol.types.internal.TypesModule
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
class InlineScriptTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    InlineImplFactory inlineFactory

    @Test
    void "inline service"() {
        def testCases = [
            [
                input: """
service "inline" with {
    script << "shell 'echo Test' call()"
}
""",
                expected: { HostServices services ->
                    assert services.getServices('inline').size() == 1
                    Inline service = services.getServices('inline')[0] as Inline
                    assert service.scripts.size() == 1
                    assert service.scripts[0] == "shell 'echo Test' call()"
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            log.info '\n#### {}. case: {}', k, test
            def services = servicesFactory.create()
            services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
            services.putAvailableService 'inline', inlineFactory
            Eval.me 'service', services, test.input as String
            Closure expected = test.expected
            expected services
        }
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new InlineModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new DurationFormatModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
