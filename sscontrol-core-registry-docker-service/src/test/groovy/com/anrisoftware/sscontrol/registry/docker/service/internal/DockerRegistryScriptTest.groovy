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
package com.anrisoftware.sscontrol.registry.docker.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.registry.docker.service.external.DockerRegistry
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
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
class DockerRegistryScriptTest {

    @Inject
    DockerRegistryImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "client_certs"() {
        def test = [
            name: 'client_certs',
            input: '''
service "registry-docker", group: "default" with {
    host address: "docker:2376", ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    registry port: 5000, ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    credentials "user", name: "devent", password: "xx"
}
''',
            expected: { HostServices services ->
                assert services.getServices('registry-docker').size() == 1
                DockerRegistry s = services.getServices('registry-docker')[0]
                assert s.name == 'registry-docker'
                assert s.group == 'default'
                assert s.host.address.toString() == 'docker:2376'
                assert s.host.client.ca.toString() == 'file:ca.pem'
                assert s.registry.port == 5000
                assert s.registry.client.ca.toString() == 'file:ca.pem'
                assert s.credentials.type == 'user'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'registry-docker', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new DockerRegistryModule(),
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
                new TlsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
