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
package com.anrisoftware.sscontrol.hosts.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.shell.external.Shell
import com.anrisoftware.sscontrol.shell.internal.ShellModule
import com.anrisoftware.sscontrol.shell.internal.ShellImpl.ShellImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.AbstractModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ShellScriptTest {

    @Inject
    ShellImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "shell script"() {
        def test = [
            name: 'shell_script',
            input: """
service "shell" with {
    script "echo Hello"
    script << "echo Hello"
}
""",
            expected: { HostServices services ->
                assert services.getServices('shell').size() == 1
                Shell s = services.getServices('shell')[0]
                assert s.name == 'shell'
                assert s.scripts.size() == 2
                com.anrisoftware.sscontrol.shell.external.Script script = s.scripts[0]
                assert script.vars.command =~ /echo.*/
            },
        ]
        doTest test
    }

    @Test
    void "shell privileged"() {
        def test = [
            name: 'shell_privileged',
            input: """
service "shell", privileged: true with {
    script "echo Hello"
    script << "echo Hello"
}
""",
            expected: { HostServices services ->
                assert services.getServices('shell').size() == 1
                Shell s = services.getServices('shell')[0]
                assert s.name == 'shell'
                assert s.scripts.size() == 2
                com.anrisoftware.sscontrol.shell.external.Script script = s.scripts[0]
                assert script.vars.command =~ /echo.*/
                assert script.vars.privileged == true
            },
        ]
        doTest test
    }

    @Test
    void "shell args"() {
        def test = [
            name: 'shell args',
            input: """
service "shell", privileged: true with {
    script timeout: "PT10M", command: "echo Hello"
}
""",
            expected: { HostServices services ->
                assert services.getServices('shell').size() == 1
                Shell s = services.getServices('shell')[0]
                assert s.name == 'shell'
                assert s.scripts.size() == 1
                com.anrisoftware.sscontrol.shell.external.Script script = s.scripts[0]
                assert script.vars.command =~ /echo.*/
                assert script.vars.privileged == true
                assert script.vars.timeout == 'PT10M'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'shell', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new ShellModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new TargetsServiceModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new SystemNameMappingsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostServicePropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
