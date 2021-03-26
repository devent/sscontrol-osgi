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
package com.anrisoftware.sscontrol.ssh.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.host.external.SystemInfo
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
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
class SshScriptTest {

    @Inject
    SshImplFactory sshFactory

    @Test
    void "ssh_debug_error"() {
        def test = [
            name: "debug_error",
            input: """
ssh.with {
    debug "error", facility: 'auth', level: 1
}
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.debugLogging.modules.size() == 1
            },
        ]
        doSshTest test
    }

    @Test
    void "ssh_debug_shift"() {
        def test = [
            name: "debug_shift",
            input: """
ssh.with {
    debug << [name: "error", level: 1]
}
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.debugLogging.modules.size() == 1
            },
        ]
        doSshTest test
    }

    @Test
    void "ssh_debug_shift_map"() {
        def test = [
            name: "debug_shift_map",
            input: """
debugLogs = []
debugLogs << [name: "error", level: 1]
debugLogs.each { ssh.debug it }
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.debugLogging.modules.size() == 1
            },
        ]
        doSshTest test
    }

    @Test
    void "ssh_group"() {
        def test = [
            name: "group",
            input: """
ssh.with {
    group "nodes"
}
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.group == 'nodes'
            },
        ]
        doSshTest test
    }

    @Test
    void "ssh_hosts"() {
        def test = [
            name: "hosts",
            input: """
ssh.with {
    host "localhost"
    host "user@192.168.0.2"
    host "user@192.168.0.3:22"
}
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.hosts.size() == 3
                int i = 0
                SshHost host = ssh.hosts[i++]
                assert host.user == null
                assert host.host == 'localhost'
                assert host.port == null
                assert host.key == null
                host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.2'
                assert host.port == null
                assert host.key == null
                host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.3'
                assert host.port == 22
                assert host.key == null
            },
        ]
        doSshTest test
    }

    @Test
    void "ssh_hosts_shift"() {
        def test = [
            name: "hosts_shift",
            input: """
ssh.with {
    host << "192.168.0.1"
    host << "user@192.168.0.2"
    host << "user@192.168.0.3:22"
}
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.hosts.size() == 3
                int i = 0
                SshHost host = ssh.hosts[i++]
                assert host.user == null
                assert host.host == '192.168.0.1'
                assert host.port == null
                assert host.key == null
                host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.2'
                assert host.port == null
                assert host.key == null
                host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.3'
                assert host.port == 22
                assert host.key == null
            },
        ]
        doSshTest test
    }

    @Test
    void "ssh_hosts_shift_map"() {
        def test = [
            name: "hosts_shift_map",
            input: """
sshHosts = []
sshHosts << [host: "192.168.0.1", user: "user", key: "user.pub"]
sshHosts << [host: "192.168.0.2", user: "user", key: "user.pub"]
sshHosts << [host: "192.168.0.3", user: "user", key: "user.pub"]
sshHosts.each { ssh.host it }
ssh
""",
            expected: { Ssh ssh ->
                assert ssh.hosts.size() == 3
                int i = 0
                SshHost host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.1'
                assert host.port == null
                assert host.key == new URI('file:user.pub')
                host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.2'
                assert host.port == null
                assert host.key == new URI('file:user.pub')
                host = ssh.hosts[i++]
                assert host.user == 'user'
                assert host.host == '192.168.0.3'
                assert host.port == null
                assert host.key == new URI('file:user.pub')
            },
        ]
        doSshTest test
    }

    void doSshTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def ssh = Eval.me 'ssh', sshFactory.create([:]), test.input as String
        log.info 'ssh: {}', ssh
        Closure expected = test.expected
        expected ssh
    }

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "service_host_arg"() {
        def test = [
            name: "host_arg",
            input: """
service "ssh", host: "192.168.0.2"
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == "default"
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_host_key_arg"() {
        def test = [
            name: "host_key_arg",
            input: """
service "ssh", host: "192.168.0.2", key: "robobee_id_rsa"
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == "default"
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                assert host.key.toString() == "file:robobee_id_rsa"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_host"() {
        def test = [
            name: "host",
            input: """
service "ssh" with {
    host "192.168.0.2"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == "default"
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_host_key"() {
        def test = [
            name: "host",
            input: """
service "ssh", key: "id_rsa" with {
    host "192.168.0.2"
    host "192.168.0.3"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == "default"
                assert ssh.hosts.size() == 2
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                assert host.key == new URI("file:id_rsa")
                host = ssh.hosts[1]
                assert host.host == "192.168.0.3"
                assert host.key == new URI("file:id_rsa")
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_group_arg_host"() {
        def test = [
            name: "group_arg_host",
            input: """
service "ssh", group: "master" with {
    host "192.168.0.2"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'master'
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_host_system_arg"() {
        def test = [
            name: "host_system_arg",
            input: """
service "ssh", host: "192.168.0.2", system: "debian/8"
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'default'
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                SystemInfo sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "8"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_system_arg_hosts"() {
        def test = [
            name: "system_arg_hosts",
            input: """
service "ssh", system: "debian/8" with {
    host "192.168.0.2"
    host "192.168.0.3"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'default'
                assert ssh.hosts.size() == 2
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                SystemInfo sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "8"
                host = ssh.hosts[1]
                assert host.host == "192.168.0.3"
                sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "8"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_hosts"() {
        def test = [
            name: "service_hosts",
            input: """
service "ssh" with {
    host host: "192.168.0.2", system: "debian/8"
    host host: "192.168.0.3", system: "debian/9"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'default'
                assert ssh.hosts.size() == 2
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                SystemInfo sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "8"
                host = ssh.hosts[1]
                assert host.host == "192.168.0.3"
                sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "9"
            },
        ]
        doServiceTest test
    }

    @Test
    void "service_hosts_system"() {
        def test = [
            name: "service_hosts_system",
            input: """
service "ssh" with {
    host "192.168.0.2", system: "debian/8"
    host "192.168.0.3", system: "debian/9"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'default'
                assert ssh.hosts.size() == 2
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                SystemInfo sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "8"
                host = ssh.hosts[1]
                assert host.host == "192.168.0.3"
                sys = host.system
                assert sys.system == "linux"
                assert sys.name == "debian"
                assert sys.version == "9"
            },
        ]
        doServiceTest test
    }

    @Test
    void "ssh_socket_host_arg"() {
        def test = [
            name: "ssh_socket_host_arg",
            input: """
service "ssh" with {
    host "192.168.0.2", socket: "/tmp/socket"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'default'
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                assert host.socket == new File("/tmp/socket")
            },
        ]
        doServiceTest test
    }

    @Test
    void "ssh_socket_arg"() {
        def test = [
            name: "ssh_socket_arg",
            input: """
service "ssh", socket: "/tmp/socket" with {
    host "192.168.0.2"
}
""",
            expected: { HostServices services ->
                assert services.getServices('ssh').size() == 1
                Ssh ssh = services.getServices('ssh')[0] as Ssh
                assert ssh.group == 'default'
                assert ssh.hosts.size() == 1
                SshHost host = ssh.hosts[0]
                assert host.host == "192.168.0.2"
                assert host.socket == new File("/tmp/socket")
            },
        ]
        doServiceTest test
    }

    void doServiceTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.putAvailableService 'ssh', sshFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new SshModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
