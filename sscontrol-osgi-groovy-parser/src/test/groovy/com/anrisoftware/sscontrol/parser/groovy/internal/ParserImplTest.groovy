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
package com.anrisoftware.sscontrol.parser.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnamePreModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.hostname.service.internal.HostnamePreScriptImpl.HostnamePreScriptImplFactory
import com.anrisoftware.sscontrol.parser.groovy.internal.ParserImpl.ParserImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.AbstractModule
import com.google.inject.Guice

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
@CompileStatic
class ParserImplTest {

    @Inject
    ParserImplFactory scriptsFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    HostnameImplFactory hostnameFactory

    @Inject
    HostnamePreScriptImplFactory hostnamePreFactory

    @Test
    void "parse script"() {
        def parent = hostnameScript.toString()
        int index = parent.lastIndexOf '/'
        parent = parent.substring(0, index + 1)
        def roots = [new URI(parent)] as URI[]
        def name = 'HostnameScript.groovy'
        def variables = [:]
        def hostServices = servicesFactory.create()
        hostServices.putAvailableService 'ssh', sshFactory
        hostServices.putAvailableService 'hostname', hostnameFactory
        hostServices.putAvailablePreService 'hostname', hostnamePreFactory
        def parser = scriptsFactory.create(roots, name, variables, hostServices)
        parser.parse()
        assert hostServices.services.size() == 2
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new ParserModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new TypesModule(),
                new StringsModule(),
                new DebugLoggingModule(),
                new SshModule(),
                new HostnameModule(),
                new HostnamePreModule(),
                new PropertiesModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }

    static final URI hostnameScript = ParserImplTest.class.getResource('HostnameScript.groovy').toURI()
}
