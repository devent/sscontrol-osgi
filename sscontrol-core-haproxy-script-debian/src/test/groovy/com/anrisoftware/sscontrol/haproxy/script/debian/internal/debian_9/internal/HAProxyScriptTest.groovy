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
package com.anrisoftware.sscontrol.haproxy.script.debian.internal.debian_9.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@EnabledIfSystemProperty(named = 'project.custom.local.tests.enabled', matches = 'true')
@ExtendWith(LocalhostSocketCondition.class)
class HAProxyScriptTest extends AbstractHAProxyScriptTest {

    @Test
    void "haproxy_script_proxies"() {
        def test = [
            name: "haproxy_script_proxies",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "haproxy", version: "1.8" with {
    proxy "http" with {
        backend address: "192.168.56.201", port: 30000
    }
    proxy "https" with {
        frontend name: "andrea-node-1", address: "192.168.56.201"
        backend address: "192.168.56.201", port: 30001
    }
    proxy "ssh" with {
        frontend name: "andrea-node-1", address: "192.168.56.201", port: 22
        backend address: "192.168.56.201", port: 30022
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/haproxy.cfg", "${args.test.name}_haproxy_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/http.cfg", "${args.test.name}_http_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/https.cfg", "${args.test.name}_https_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/ssh.cfg", "${args.test.name}_ssh_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "chown.out", "${args.test.name}_chown_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "chmod.out", "${args.test.name}_chmod_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
                //assertFileResource CollectdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_default_frontends"() {
        def test = [
            name: "script_default_frontends",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "haproxy", version: "1.8" with {
    proxy "http" with {
        backend address: "192.168.56.201", port: 30000
    }
    proxy "https" with {
        backend address: "192.168.56.201", port: 30001
    }
    proxy "ssh" with {
        backend address: "192.168.56.201", port: 30022
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/haproxy.cfg", "${args.test.name}_haproxy_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/http.cfg", "${args.test.name}_http_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/https.cfg", "${args.test.name}_https_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/ssh.cfg", "${args.test.name}_ssh_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_address_frontends"() {
        def test = [
            name: "script_address_frontends",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "haproxy", version: "1.8" with {
    proxy "http" with {
        frontend address: "192.168.56.201"
        backend address: "192.168.56.201", port: 30000
    }
    proxy "https" with {
        frontend address: "192.168.56.201"
        backend address: "192.168.56.201", port: 30001
    }
    proxy "ssh" with {
        frontend address: "192.168.56.201"
        backend address: "192.168.56.201", port: 30022
    }
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/haproxy.cfg", "${args.test.name}_haproxy_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/http.cfg", "${args.test.name}_http_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/https.cfg", "${args.test.name}_https_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, gen, "/etc/haproxy/conf.d/ssh.cfg", "${args.test.name}_ssh_cfg_expected.txt"
                assertFileResource HAProxyScriptTest, dir, "ufw.out", "${args.test.name}_ufw_expected.txt"
            },
        ]
        doTest test
    }
}
