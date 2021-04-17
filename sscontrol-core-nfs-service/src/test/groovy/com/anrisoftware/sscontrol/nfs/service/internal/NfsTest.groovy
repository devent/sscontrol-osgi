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
package com.anrisoftware.sscontrol.nfs.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.nfs.service.external.Nfs
import com.anrisoftware.sscontrol.nfs.service.internal.NfsImplFactory
import com.anrisoftware.sscontrol.nfs.service.internal.NfsModule
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
class NfsTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    NfsImplFactory serviceFactory

    @Test
    void "service_defaults"() {
        def test = [
            name: 'service_defaults',
            input: '''
service "nfs", version: "1.3" with {
    export dir: "/nfsfileshare/0" with {
        host << "andrea-node-0.muellerpublic.de"
        host name: "andrea-node-1.muellerpublic.de", options: "rw,sync,no_root_squash"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('nfs').size() == 1
                Nfs collectd = services.getServices('nfs')[0]
                assert collectd.version == '1.3'
                assert collectd.exports.size() == 1
                assert collectd.exports[0].dir == '/nfsfileshare/0'
                assert collectd.exports[0].hosts.size() == 2
                assert collectd.exports[0].hosts[0].name == 'andrea-node-0.muellerpublic.de'
                assert collectd.exports[0].hosts[0].options == null
                assert collectd.exports[0].hosts[1].name == 'andrea-node-1.muellerpublic.de'
                assert collectd.exports[0].hosts[1].options == 'rw,sync,no_root_squash'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'nfs', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new NfsModule(),
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
