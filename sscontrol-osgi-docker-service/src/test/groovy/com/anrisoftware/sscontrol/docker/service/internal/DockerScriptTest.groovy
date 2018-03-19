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
package com.anrisoftware.sscontrol.docker.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.docker.service.external.Docker
import com.anrisoftware.sscontrol.docker.service.external.Mirror
import com.anrisoftware.sscontrol.docker.service.internal.DockerImpl.DockerImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.tls.internal.TlsModule
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
class DockerScriptTest {

    @Inject
    DockerImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "basic service"() {
        def test = [
            name: 'basic service',
            input: """
service "docker"
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s != null
            },
        ]
        doTest test
    }

    @Test
    void "log driver"() {
        def test = [
            name: "log driver",
            input: """
service "docker" with {
    log driver: 'json-file', maxSize: "10m", maxFile: 10
}
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s != null
                assert s.loggingDriver.driver == "json-file"
                assert s.loggingDriver.opts.size() == 2
                assert s.loggingDriver.opts.maxSize == "10m"
                assert s.loggingDriver.opts.maxFile == 10
            },
        ]
        doTest test
    }

    @Test
    void "groups_shift"() {
        def test = [
            name: 'groups_shift',
            input: """
service "docker" with {
    groups << 'memory'
}
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s.cgroups.size() == 1
                assert s.cgroups[0] == "memory"
            },
        ]
        doTest test
    }

    @Test
    void "registry_mirror"() {
        def test = [
            name: 'registry_mirror',
            input: """
service "docker" with {
    registry mirror: 'localhost:5000'
}
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s.registry.mirrorHosts.size() == 1
                Mirror m = s.registry.mirrorHosts[0]
                assert m.host.toString() == 'localhost:5000'
                assert m.tls.ca == null
                assert m.tls.cert == null
                assert m.tls.key == null
            },
        ]
        doTest test
    }

    @Test
    void "registry_mirror_host"() {
        def test = [
            name: 'registry_mirror_host',
            input: """
service "docker" with {
    registry mirror: 'localhost'
}
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s.registry.mirrorHosts.size() == 1
                Mirror m = s.registry.mirrorHosts[0]
                assert m.host.toString() == 'localhost'
                assert m.tls.ca == null
                assert m.tls.cert == null
                assert m.tls.key == null
            },
        ]
        doTest test
    }

    @Test
    void "registry_mirror_ca"() {
        def test = [
            name: 'registry_mirror_ca',
            input: """
service "docker" with {
    registry mirror: 'localhost:5000', ca: 'ca.pem'
}
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s.registry.mirrorHosts.size() == 1
                Mirror m = s.registry.mirrorHosts[0]
                assert m.host.toString() == 'localhost:5000'
                assert m.tls.ca.toString() == 'file:ca.pem'
                assert m.tls.cert == null
                assert m.tls.key == null
            },
        ]
        doTest test
    }

    @Test
    void "cgroup_driver"() {
        def test = [
            name: 'cgroup_driver',
            input: """
service "docker" with {
    property << "native_cgroupdriver=systemd"
}
""",
            expected: { HostServices services ->
                assert services.getServices('docker').size() == 1
                Docker s = services.getServices('docker')[0]
                assert s.serviceProperties.getProperty("native_cgroupdriver") == "systemd"
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'docker', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new DockerModule(),
                new DockerPreModule(),
                new DebugLoggingModule(),
                new PropertiesModule(),
                new HostServicesModule(),
                new StringsModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
