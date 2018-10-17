/*-
 * #%L
 * sscontrol-osgi - script-runner
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.runner.groovy.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.sscontrol.hostname.script.debian.internal.debian_9.Hostname_Debian_9_Factory
import com.anrisoftware.sscontrol.hostname.script.debian.internal.debian_9.Hostname_Debian_9_Module
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnamePreModule
import com.anrisoftware.sscontrol.hostname.service.internal.HostnameImpl.HostnameImplFactory
import com.anrisoftware.sscontrol.hostname.service.internal.HostnamePreScriptImpl.HostnamePreScriptImplFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.runner.test.external.AbstractRunnerTestBase
import com.anrisoftware.sscontrol.ssh.script.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.script.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtilsModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class RunnerDebianImplTest extends AbstractRunnerTestBase {

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    HostnameImplFactory hostnameFactory

    @Inject
    HostnamePreScriptImplFactory hostnamePreFactory

    @Inject
    Hostname_Debian_9_Factory hostnameDebianFactory

    static final URL hostnameScript = RunnerDebianImplTest.class.getResource('HostnameScript.groovy')

    @Test
    void "run scripts"() {
        def test = [
            name: "default_target",
            script: hostnameScript,
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
            },
        ]
        doTest test, 0
    }

    @BeforeEach
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'hostname', hostnameFactory
        services.putAvailablePreService 'hostname', hostnamePreFactory
        services.putAvailableScriptService 'hostname/debian/9', hostnameDebianFactory
        return services
    }

    void createDummyCommands(File dir) {
        createCommand grepCommand, dir, "grep"
        createCommand catCommand, dir, "cat"
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
        modules << new RunnerModule()
        modules << new HostnameModule()
        modules << new HostnamePreModule()
        modules << new Hostname_Debian_9_Module()
        modules << new SshModule()
        modules << new Ssh_Linux_Module()
        modules << new SystemNameMappingsModule()
        modules << new DebianUtilsModule()
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
