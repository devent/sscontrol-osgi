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
package com.anrisoftware.sscontrol.flanneldocker.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.flanneldocker.external.FlannelDocker
import com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImpl.FlannelDockerImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
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
class FlannelDockerScriptTest {

    @Inject
    FlannelDockerImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "etcd"() {
        def test = [
            name: 'etcd',
            input: """
service "flannel-docker" with {
    etcd endpoints: "http://127.0.0.1:2379", prefix: "/atomic.io/network"
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.etcd.endpoints.size() == 1
                assert s.etcd.endpoints[0] == "http://127.0.0.1:2379"
                assert s.etcd.prefix == "/atomic.io/network"
            },
        ]
        doTest test
    }

    @Test
    void "etcd_endpoint"() {
        def test = [
            name: 'etcd_endpoint',
            input: """
service "flannel-docker" with {
    etcd "http://127.0.0.1:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.etcd.endpoints.size() == 1
                assert s.etcd.endpoints[0] == "http://127.0.0.1:2379"
                assert s.etcd.prefix == null
            },
        ]
        doTest test
    }

    @Test
    void "multiple_etcd_endpoints"() {
        def test = [
            name: 'multiple_etcd_endpoints',
            input: """
service "flannel-docker" with {
    etcd endpoints: "https://etcd-0:2379,https://etcd-1:2379,https://etcd-2:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.etcd.endpoints.size() == 3
                assert s.etcd.endpoints[0] == "https://etcd-0:2379"
                assert s.etcd.address == "https://etcd-0:2379"
                assert s.etcd.prefix == null
            },
        ]
        doTest test
    }

    @Test
    void "etcd_address"() {
        def test = [
            name: 'etcd_address',
            input: """
service "flannel-docker" with {
    etcd address: "https://etcd-1:2379", endpoints: "https://etcd-0:2379,https://etcd-1:2379,https://etcd-2:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.etcd.endpoints.size() == 3
                assert s.etcd.endpoints[0] == "https://etcd-0:2379"
                assert s.etcd.address == "https://etcd-1:2379"
                assert s.etcd.prefix == null
            },
        ]
        doTest test
    }

    @Test
    void "network"() {
        def test = [
            name: 'network',
            input: """
service "flannel-docker" with {
    network "10.2.0.0/16"
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.network.address == "10.2.0.0/16"
            },
        ]
        doTest test
    }

    @Test
    void "backend_vxlan"() {
        def test = [
            name: 'backend_vxlan',
            input: """
service "flannel-docker" with {
    backend "vxlan"
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.backend.type == "vxlan"
            },
        ]
        doTest test
    }

    @Test
    void "etcd tls"() {
        def test = [
            name: "etcd tls",
            input: """
service "flannel-docker" with {
    etcd "https://127.0.0.1:2379" with {
        tls ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
    }
}
""",
            expected: { HostServices services ->
                assert services.getServices('flannel-docker').size() == 1
                FlannelDocker s = services.getServices('flannel-docker')[0]
                assert s.etcd.endpoints.size() == 1
                assert s.etcd.endpoints[0] == "https://127.0.0.1:2379"
                assert s.etcd.prefix == null
                Tls tls = s.etcd.tls
                assert tls.ca == new URI('file:ca.pem')
                assert tls.cert == new URI('file:cert.pem')
                assert tls.key == new URI('file:key.pem')
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'flannel-docker', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new FlannelDockerModule(),
                new FlannelDockerPreModule(),
                new PropertiesModule(),
                new TlsModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
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
