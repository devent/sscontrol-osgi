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
package com.anrisoftware.sscontrol.collectd.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.collectd.service.external.Collectd
import com.anrisoftware.sscontrol.collectd.service.internal.CollectdImpl.CollectdImplFactory
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
class CollectdTest {

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    CollectdImplFactory serviceFactory

    @Test
    void "config_shift"() {
        def test = [
            name: 'config_shift',
            input: '''
service "collectd", version: "5.7" with {
    config << [name: "99-write_graphite", script: """
LoadPlugin "write_graphite"
<Plugin "write_graphite">
<Node "graphite">
  Host "graphite_carbon-2003.service.consul.emwork.nttdata.com"
  Port "2003"
  Prefix "collectd."
  #Postfix ""
  Protocol "tcp"
  LogSendErrors true
  EscapeCharacter "_"
  SeparateInstances true
  StoreRates true
  AlwaysAppendDS false
</Node>
</Plugin>
"""]
}
''',
            expected: { HostServices services ->
                assert services.getServices('collectd').size() == 1
                Collectd collectd = services.getServices('collectd')[0]
                assert collectd.version == '5.7'
                assert collectd.configs.size() == 1
                assert collectd.configs[0].name == '99-write_graphite'
                assert collectd.configs[0].script =~ /LoadPlugin.*/
            },
        ]
        doTest test
    }

    @Test
    void "config_args"() {
        def test = [
            name: 'config_args',
            input: '''
service "collectd", version: "5.7" with {
    config name: "99-write_graphite", script: """
LoadPlugin "write_graphite"
<Plugin "write_graphite">
<Node "graphite">
  Host "graphite_carbon-2003.service.consul.emwork.nttdata.com"
  Port "2003"
  Prefix "collectd."
  #Postfix ""
  Protocol "tcp"
  LogSendErrors true
  EscapeCharacter "_"
  SeparateInstances true
  StoreRates true
  AlwaysAppendDS false
</Node>
</Plugin>
"""
}
''',
            expected: { HostServices services ->
                assert services.getServices('collectd').size() == 1
                Collectd collectd = services.getServices('collectd')[0]
                assert collectd.version == '5.7'
                assert collectd.configs.size() == 1
                assert collectd.configs[0].name == '99-write_graphite'
                assert collectd.configs[0].script =~ /LoadPlugin.*/
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'collectd', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new CollectdModule(),
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
