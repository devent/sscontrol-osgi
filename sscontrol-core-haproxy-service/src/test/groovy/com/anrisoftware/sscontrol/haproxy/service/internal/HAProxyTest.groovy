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
package com.anrisoftware.sscontrol.haproxy.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.haproxy.service.external.HAProxy
import com.anrisoftware.sscontrol.haproxy.service.internal.HAProxyImplFactory
import com.anrisoftware.sscontrol.haproxy.service.internal.HAProxyModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
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
class HAProxyTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HAProxyImplFactory serviceFactory

    @Test
    void "service_defaults"() {
        def test = [
            name: 'service_defaults',
            input: '''
service "haproxy", version: "1.8" with {
    proxy "http" with {
        backend address: "192.168.56.201", port: 30000
    }
    proxy "https" with {
        frontend name: "andrea-node-1", address: "192.168.56.201"
        backend address: "192.168.56.201", port: 30001
    }
    proxy "ssh" with {
        frontend name: "andrea-node-1", address: "192.168.56.201", port: 22
        backend address: "192.168.56.201", port: 30022
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('haproxy').size() == 1
                HAProxy haproxy = services.getServices('haproxy')[0]
                assert haproxy.version == '1.8'
                assert haproxy.proxies.size() == 3
                int k = 0
                assert haproxy.proxies[k].name == 'http'
                assert haproxy.proxies[k].frontend == null
                assert haproxy.proxies[k].backend.address == '192.168.56.201'
                assert haproxy.proxies[k].backend.port == 30000
                k++
                assert haproxy.proxies[k].name == 'https'
                assert haproxy.proxies[k].frontend.name == 'andrea-node-1'
                assert haproxy.proxies[k].frontend.address == '192.168.56.201'
                assert haproxy.proxies[k].frontend.port == null
                assert haproxy.proxies[k].backend.address == '192.168.56.201'
                assert haproxy.proxies[k].backend.port == 30001
                k++
                assert haproxy.proxies[k].name == 'ssh'
                assert haproxy.proxies[k].frontend.name == 'andrea-node-1'
                assert haproxy.proxies[k].frontend.address == '192.168.56.201'
                assert haproxy.proxies[k].frontend.port == 22
                assert haproxy.proxies[k].backend.address == '192.168.56.201'
                assert haproxy.proxies[k].backend.port == 30022
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'haproxy', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new HAProxyModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
