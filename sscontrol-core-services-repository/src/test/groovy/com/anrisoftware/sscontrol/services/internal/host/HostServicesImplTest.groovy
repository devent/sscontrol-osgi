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
/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.services.internal.host

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostnameStub.HostnameStubFactory
import com.anrisoftware.sscontrol.services.internal.host.HostnameStub.HostnameStubServiceImpl
import com.anrisoftware.sscontrol.services.internal.host.HostsStub.Host
import com.anrisoftware.sscontrol.services.internal.host.HostsStub.HostsStubFactory
import com.anrisoftware.sscontrol.services.internal.host.HostsStub.HostsStubServiceImpl
import com.anrisoftware.sscontrol.services.internal.host.SshStub.SshStubFactory
import com.anrisoftware.sscontrol.services.internal.host.SshStub.SshStubServiceImpl
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.assistedinject.FactoryModuleBuilder

import groovy.util.logging.Slf4j

/**
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class HostServicesImplTest {

    Injector injector

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    HostnameStubServiceImpl hostnameService

    @Inject
    SshStubServiceImpl sshService

    @Inject
    HostsStubServiceImpl hostsService

    @Test
    void "default hostname"() {
        def test = [
            input: '''
service 'ssh', host: "192.168.0.3"

service 'hostname' with {
    // Sets the hostname.
    set "blog.muellerpublic.de"
}
''',
            expected: { HostServices services ->
                assert services.getServices('hostname').size() == 1
                HostnameStub service = services.getServices('hostname')[0] as HostnameStub
                assert service.name == 'blog.muellerpublic.de'
            },
        ]
        doTest test
    }

    @Test
    void "hosts by nodes by name"() {
        def test = [
            input: '''
service 'ssh' with {
    group "nodes"
    host "192.168.0.3"
    host "192.168.0.4"
    host "192.168.0.5"
}

service 'hosts', target: 'nodes' with {
    targets.eachWithIndex { h, i ->
        host "node-${i}.muellerpublic.de", "$h.host"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('hosts').size() == 1
                int i = 0
                HostsStub service = services.getServices('hosts')[i++] as HostsStub
                int k = 0
                Host host = service.hosts[k++] as Host
                assert host.host == 'node-0.muellerpublic.de'
                host = service.hosts[k++] as Host
                assert host.host == 'node-1.muellerpublic.de'
                host = service.hosts[k++] as Host
                assert host.host == 'node-2.muellerpublic.de'
            },
        ]
        doTest test
    }

    @Test
    void "hostname by nodes list"() {
        def test = [
            input: '''
def nodes = service 'ssh' with {
    group "nodes"
    host "192.168.0.3"
    host "192.168.0.4"
    host "192.168.0.5"
    it
}

service 'hostname', target: nodes with {
    set "node.muellerpublic.de"
}
''',
            expected: { HostServices services ->
                assert services.getServices('hostname').size() == 1
                int i = 0
                HostnameStub service = services.getServices('hostname')[i++] as HostnameStub
                assert service.name == 'node.muellerpublic.de'
            },
        ]
        doTest test
    }

    @Test
    void "hostname by nodes by object"() {
        def test = [
            input: '''
service 'ssh' with {
    group "nodes"
    host "192.168.0.3"
    host "192.168.0.4"
    host "192.168.0.5"
}
targets.call('nodes').eachWithIndex { host, i ->
    service 'hostname', target: host with {
        set "node-${i}.muellerpublic.de"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('hostname').size() == 3
                int i = 0
                HostnameStub service = services.getServices('hostname')[i++] as HostnameStub
                assert service.name == 'node-0.muellerpublic.de'
                service = services.getServices('hostname')[i++] as HostnameStub
                assert service.name == 'node-1.muellerpublic.de'
                service = services.getServices('hostname')[i++] as HostnameStub
                assert service.name == 'node-2.muellerpublic.de'
            },
        ]
        doTest test
    }

    def doTest(Map test, int k=0) {
        log.info '{}. case: {}', k, test
        def services = servicesFactory.create()
        def targets = services.getTargets()
        services.putAvailableService 'hostname', hostnameService
        services.putAvailableService 'ssh', sshService
        services.putAvailableService 'hosts', hostsService
        eval service: services, targets: targets, test.input as String
        Closure expected = test.expected
        expected services
    }

    def eval(Map args, String script) {
        def b = new Binding()
        args.each { name, value ->
            b.setVariable name as String, value
        }
        def sh = new GroovyShell(b)
        sh.evaluate script
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        this.injector = Guice.createInjector(
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new TypesModule(),
                new StringsModule(),
                new SystemNameMappingsModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        install(new FactoryModuleBuilder().implement(HostnameStub, HostnameStub).build(HostnameStubFactory))
                        install(new FactoryModuleBuilder().implement(SshStub, SshStub).build(SshStubFactory))
                        install(new FactoryModuleBuilder().implement(HostsStub, HostsStub).build(HostsStubFactory))
                    }
                })
        injector.injectMembers(this)
    }
}
