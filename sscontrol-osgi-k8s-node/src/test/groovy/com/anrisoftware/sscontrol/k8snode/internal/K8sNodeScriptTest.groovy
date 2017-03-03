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
package com.anrisoftware.sscontrol.k8snode.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8snode.external.K8sNode
import com.anrisoftware.sscontrol.k8snode.internal.K8sNodeImpl.K8sNodeImplFactory
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
class K8sNodeScriptTest {

    @Inject
    K8sNodeImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "master_target"() {
        def test = [
            name: 'master_target',
            input: """
service "k8s-node" with {
    master target: "master"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0] as K8sNode
                assert s.targets.size() == 0
                assert s.master.target == 'master'
            },
        ]
        doTest test
    }

    @Test
    void "master_address"() {
        def test = [
            name: 'master_address',
            input: """
service "k8s-node" with {
    master address: "master"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0] as K8sNode
                assert s.targets.size() == 0
                assert s.master.target == null
                assert s.master.address == 'master'
            },
        ]
        doTest test
    }

    @Test
    void "tls"() {
        def test = [
            name: 'tls',
            input: """
service "k8s-node" with {
    master address: "master"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0] as K8sNode
                assert s.targets.size() == 0
                assert s.master.target == null
                assert s.master.address == 'master'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'k8s-node', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new K8sNodeModule(),
                new K8sNodePreModule(),
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
