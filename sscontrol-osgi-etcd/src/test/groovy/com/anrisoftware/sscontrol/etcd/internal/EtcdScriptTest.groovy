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

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.etcd.external.Etcd
import com.anrisoftware.sscontrol.etcd.internal.EtcdImpl.EtcdImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService
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
    void "bind_address"() {
        def test = [
            name: 'bind_address',
            input: """
service "etcd" with {
    bind address: "http://localhost:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.bindings.size() == 1
                assert s.bindings[0].address == new URI("http://localhost:2379")
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
    bind "http://localhost:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.bindings.size() == 1
                assert s.bindings[0].address == new URI("http://localhost:2379")
            },
        ]
        doTest test
    }

    @Test
    void "advertise_address"() {
        def test = [
            name: 'advertise_address',
            input: """
service "etcd" with {
    advertise address: "http://localhost:2380"
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.advertises.size() == 1
                assert s.advertises[0].address == new URI("http://localhost:2380")
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
    advertise "http://localhost:2380"
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.advertises.size() == 1
                assert s.advertises[0].address == new URI("http://localhost:2380")
            },
        ]
        doTest test
    }

    @Test
    void "tls"() {
        def test = [
            name: 'tls',
            input: """
service "etcd" with {
    tls cert: 'cert.pem', key: 'key.pem'
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.tls.cert.toString() == 'file:cert.pem'
                assert s.tls.key.toString() == 'file:key.pem'
            },
        ]
        doTest test
    }

    @Test
    void "authentication_args"() {
        def test = [
            name: 'authentication_args',
            input: """
service "etcd" with {
    authentication type: "cert", ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    authentication "cert", ca: "ca.pem", cert: "cert.pem", key: "key.pem"
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                assert s.authentications.size() == 2
                int k = -1
                assert s.authentications[++k].type == 'cert'
                assert s.authentications[k].ca.toString() == 'file:ca.pem'
                assert s.authentications[k].cert.toString() == 'file:cert.pem'
                assert s.authentications[k].key.toString() == 'file:key.pem'
                assert s.authentications[++k].type == 'cert'
                assert s.authentications[k].ca.toString() == 'file:ca.pem'
                assert s.authentications[k].cert.toString() == 'file:cert.pem'
                assert s.authentications[k].key.toString() == 'file:key.pem'
            },
        ]
        doTest test
    }

    @Test
    void "static_cluster"() {
        def test = [
            name: 'static_cluster',
            input: """
service "etcd" with {
    peer state: "new", advertise: "https://10.0.1.10:2380", listen: "https://10.0.1.10:2380", token: "etcd-cluster-1" with {
        cluster << "infra0=https://10.0.1.10:2380"
        cluster name: "infra1", address: "https://10.0.1.11:2380"
        cluster "infra2", address: "https://10.0.1.12:2380"
        tls cert: "cert.pem", key: "key.pem"
        authentication "cert", ca: "ca.pem", cert: 'cert.pem', key: 'key.pem'
    }
}
""",
            expected: { HostServices services ->
                assert services.getServices('etcd').size() == 1
                Etcd s = services.getServices('etcd')[0]
                s.tls.ca == null
                s.tls.cert == null
                s.tls.key == null
                assert s.peer.state == 'new'
                assert s.peer.advertises.size() == 1
                assert s.peer.advertises[0].address == new URI('https://10.0.1.10:2380')
                assert s.peer.listens.size() == 1
                assert s.peer.listens[0].address == new URI('https://10.0.1.10:2380')
                assert s.peer.clusters.size() == 3
                assert s.peer.clusters[0].name == 'infra0'
                assert s.peer.clusters[0].address.address == new URI('https://10.0.1.10:2380')
                assert s.peer.clusters[1].name == 'infra1'
                assert s.peer.clusters[1].address.address == new URI('https://10.0.1.11:2380')
                assert s.peer.clusters[2].name == 'infra2'
                assert s.peer.clusters[2].address.address == new URI('https://10.0.1.12:2380')
                assert s.peer.tls.cert == new URI('file:cert.pem')
                assert s.peer.tls.key == new URI('file:key.pem')
                assert s.peer.authentications.size() == 1
                assert s.peer.authentications[0].type == 'cert'
                assert s.peer.authentications[0].ca.toString() == 'file:ca.pem'
                assert s.peer.authentications[0].cert.toString() == 'file:cert.pem'
                assert s.peer.authentications[0].key.toString() == 'file:key.pem'
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
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
