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
package com.anrisoftware.sscontrol.etcd.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.etcd.external.Etcd
import com.anrisoftware.sscontrol.etcd.internal.EtcdImpl.EtcdImplFactory
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

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class EtcdScriptTest {

    @Inject
    EtcdImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "member_args"() {
        def test = [
            name: 'member_args',
            input: """
service "etcd", member: "default"
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.memberName == "default"
            },
        ]
        doTest test
    }

    @Test
    void "bind"() {
        def test = [
            name: 'bind',
            input: """
service "etcd" with {
    bind scheme: "http", address: "localhost", port: 2379
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.bindings.size() == 1
                assert s.bindings[0].address == "localhost"
                assert s.bindings[0].scheme == "http"
                assert s.bindings[0].port == 2379
            },
        ]
        doTest test
    }

    @Test
    void "advertise"() {
        def test = [
            name: 'advertise',
            input: """
service "etcd" with {
    advertise scheme: "http", address: "localhost", port: 2379
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.advertises.size() == 1
                assert s.advertises[0].address == "localhost"
                assert s.advertises[0].scheme == "http"
                assert s.advertises[0].port == 2379
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'etcd', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new EtcdModule(),
                new EtcdPreModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new BindingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
