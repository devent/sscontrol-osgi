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
package com.anrisoftware.sscontrol.k8scluster.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sModule
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sPreModule
import com.anrisoftware.sscontrol.k8scluster.external.K8sCluster
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterImpl.K8sClusterImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
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
class K8sClusterScriptTest {

    @Inject
    K8sClusterImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "cluster"() {
        def test = [
            name: 'cluster',
            input: """
service "k8s-cluster", group: 'default' with {
    cluster name: 'default-cluster'
    context name: 'default-system'
    credentials type: 'cert', name: 'default-admin', ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-cluster').size() == 1
                K8sCluster s = services.getServices('k8s-cluster')[0] as K8sCluster
                assert s.cluster.name == 'default-cluster'
                assert s.context.name == 'default-system'
                assert s.credentials.size() == 1
                assert s.credentials[0].type == 'cert'
                assert s.credentials[0].name == 'default-admin'
                assert s.credentials[0].tls.ca.toString() =~ /.*ca\.pem/
                assert s.credentials[0].tls.cert.toString() =~ /.*cert\.pem/
                assert s.credentials[0].tls.key.toString() =~ /.*key\.pem/
            },
        ]
        doTest test
    }

    @Test
    void "args"() {
        def test = [
            name: 'args',
            input: """
service "k8s-cluster", group: 'default', cluster: 'default-cluster', context: 'default-system' with {
    credentials type: 'cert', name: 'default-admin', ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-cluster').size() == 1
                K8sCluster s = services.getServices('k8s-cluster')[0] as K8sCluster
                assert s.cluster.name == 'default-cluster'
                assert s.context.name == 'default-system'
                assert s.credentials.size() == 1
                assert s.credentials[0].type == 'cert'
                assert s.credentials[0].name == 'default-admin'
                assert s.credentials[0].tls.ca.toString() =~ /.*ca\.pem/
                assert s.credentials[0].tls.cert.toString() =~ /.*cert\.pem/
                assert s.credentials[0].tls.key.toString() =~ /.*key\.pem/
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'k8s-cluster', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new K8sModule(),
                new K8sPreModule(),
                new K8sClusterModule(),
                new K8sClusterPreModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
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
