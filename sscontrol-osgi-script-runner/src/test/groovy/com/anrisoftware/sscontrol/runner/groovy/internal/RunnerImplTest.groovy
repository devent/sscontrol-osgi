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
package com.anrisoftware.sscontrol.runner.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8_Module
import com.anrisoftware.sscontrol.hostname.debian_8.internal.Hostname_Debian_8.Hostname_Debian_8_Factory
import com.anrisoftware.sscontrol.hostname.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.internal.HostnamePreModule
import com.anrisoftware.sscontrol.hostname.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.hostname.internal.HostnamePreScriptImpl.HostnamePreScriptImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshPreScriptImpl.SshPreScriptImplFactory
import com.anrisoftware.sscontrol.ssh.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.types.external.HostServices

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class RunnerImplTest extends AbstractRunnerTestBase {

    @Inject
    SshImplFactory sshFactory

    @Inject
    SshPreScriptImplFactory sshPreFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    HostnameImplFactory hostnameFactory

    @Inject
    HostnamePreScriptImplFactory hostnamePreFactory

    @Inject
    Hostname_Debian_8_Factory hostname_Debian_8_Factory

    static final URL hostnameScript = RunnerImplTest.class.getResource('HostnameScript.groovy')

    @Test
    void "run scripts"() {
        def testCases = [
            [
                name: "default_target",
                script: hostnameScript,
                expectedServicesSize: 2,
                expected: { Map args ->
                    File dir = args.dir
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            doTest test, k
        }
    }

    Map createVariables(Map args) {
        def a = [:]
        a.chdir = args.dir
        a.pwd = args.dir
        a.sudoEnv = [:]
        a.sudoEnv.PATH = args.dir
        a.env = [:]
        a.env.PATH = args.dir
        return a
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailablePreService 'ssh', sshPreFactory
        services.putAvailableScriptService 'ssh-linux-0', ssh_Linux_Factory
        services.putAvailableService 'hostname', hostnameFactory
        services.putAvailablePreService 'hostname', hostnamePreFactory
        services.putAvailableScriptService 'hostname-debian-8', hostname_Debian_8_Factory
        return services
    }

    void createDummyCommands(File dir) {
        createDebianJessieCatCommand dir, 'cat'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service',
            'hostnamectl',
        ]
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new HostnameModule()
        modules << new HostnamePreModule()
        modules << new Hostname_Debian_8_Module()
        modules << new SshModule()
        modules << new SshPreModule()
        modules << new Ssh_Linux_Module()
    }


    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
