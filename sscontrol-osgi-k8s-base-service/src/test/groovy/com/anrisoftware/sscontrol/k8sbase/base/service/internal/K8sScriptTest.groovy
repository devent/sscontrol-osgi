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
package com.anrisoftware.sscontrol.k8sbase.base.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.service.external.EtcdPlugin
import com.anrisoftware.sscontrol.k8sbase.base.service.external.K8s
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sImpl.K8sImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
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
class K8sScriptTest {

    @Inject
    K8sImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "cluster args"() {
        def test = [
            name: 'cluster args',
            input: """
service "k8s-master" with {
    cluster name: 'master-0', advertise: '192.168.0.1', hostname: '192.168.0.1', service: '10.3.0.0/24', pod: '10.2.0.0/16', dns: '10.3.0.10', api: 'http://localhost:8080'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8s s = services.getServices('k8s-master')[0] as K8s
                assert s.targets.size() == 0
                assert s.cluster.name == 'master-0'
                assert s.cluster.advertiseAddress == '192.168.0.1'
                assert s.cluster.serviceRange == '10.3.0.0/24'
                assert s.cluster.podRange == '10.2.0.0/16'
                assert s.cluster.dnsAddress == '10.3.0.10'
                assert s.cluster.apiServers.size() == 1
                assert s.cluster.apiServers[0] == 'http://localhost:8080'
            },
        ]
        doTest test
    }

    @Test
    void "allow privileged"() {
        def test = [
            name: 'allow privileged',
            input: """
service "k8s-master" with {
    privileged true
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8s s = services.getServices('k8s-master')[0] as K8s
                assert s.allowPrivileged == true
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin target"() {
        def test = [
            name: 'ectd plugin target',
            input: """
service "k8s-master" with {
    plugin "etcd", address: "infra-0"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8s s = services.getServices('k8s-master')[0] as K8s
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                EtcdPlugin e = s.plugins.etcd
                assert e.name == 'etcd'
                assert e.address.size() == 1
                assert e.address[0] == 'infra-0'
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin address"() {
        def test = [
            name: 'ectd plugin address',
            input: """
service "k8s-master" with {
    plugin "etcd", address: "http://etcd-0:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8s s = services.getServices('k8s-master')[0] as K8s
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                EtcdPlugin e = s.plugins.etcd
                assert e.name == 'etcd'
                assert e.address.size() == 1
                assert e.address[0] == 'http://etcd-0:2379'
            },
        ]
        doTest test
    }

    @Test
    void "kubelet"() {
        def test = [
            name: 'kubelet',
            input: '''
service "k8s-master" with {
    kubelet.with {
        tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
        preferred types: "InternalIP,Hostname,ExternalIP"
        bind port: 10250
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8s s = services.getServices('k8s-master')[0] as K8s
                assert s.kubelet.tls.ca.toString() =~ /.*ca\.pem/
                assert s.kubelet.tls.cert.toString() =~ /.*cert\.pem/
                assert s.kubelet.tls.key.toString() =~ /.*key\.pem/
                assert s.kubelet.preferredAddressTypes.size() == 3
                assert s.kubelet.port == 10250
                def k = -1
                assert s.kubelet.preferredAddressTypes[++k] == "InternalIP"
                assert s.kubelet.preferredAddressTypes[++k] == "Hostname"
                assert s.kubelet.preferredAddressTypes[++k] == "ExternalIP"
            },
        ]
        doTest test
    }

    @Test
    void "etcd tls"() {
        def test = [
            name: 'etcd tls',
            input: '''
service "k8s-master" with {
    plugin "etcd", address: "infra-0" with {
        tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8s s = services.getServices('k8s-master')[0] as K8s
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                EtcdPlugin e = s.plugins.etcd
                assert e.name == 'etcd'
                assert e.address.size() == 1
                assert e.address[0] == 'infra-0'
                assert e.tls.ca.toString() == 'file:ca.pem'
                assert e.tls.cert.toString() == 'file:cert.pem'
                assert e.tls.key.toString() == 'file:key.pem'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'k8s-master', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new K8sModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new TargetsServiceModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new SystemNameMappingsModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind(HostServicePropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}