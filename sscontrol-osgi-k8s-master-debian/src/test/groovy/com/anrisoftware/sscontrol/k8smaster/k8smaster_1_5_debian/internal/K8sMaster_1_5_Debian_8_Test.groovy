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
package com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterModule
import com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterImpl.K8sMasterImplFactory
import com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal.K8sMaster_1_5_Debian_8.K8sMaster_1_5_Debian_8_Factory
import com.anrisoftware.sscontrol.replace.internal.ReplaceModule
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class K8sMaster_1_5_Debian_8_Test extends AbstractScriptTestBase {

    @Inject
    K8sMasterImplFactory serviceFactory

    @Inject
    K8sMaster_1_5_Debian_8_Factory scriptFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Test
    void "tls"() {
        def test = [
            name: "tls",
            input: """
service "ssh", host: "localhost"

service "k8s-master", name: "andrea-cluster" with {
    tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
}
""",
            expected: { Map args ->
                File dir = args.dir
                assertFileResource K8sMaster_1_5_Debian_8_Test, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource K8sMaster_1_5_Debian_8_Test, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource K8sMaster_1_5_Debian_8_Test, dir, "service.out", "${args.test.name}_service_expected.txt"
                assertFileResource K8sMaster_1_5_Debian_8_Test, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
                assertFileResource K8sMaster_1_5_Debian_8_Test, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
            },
        ]
        doTest test
    }

    String getServiceName() {
        'k8s-master'
    }

    String getScriptServiceName() {
        'k8s-master/debian/8'
    }

    void createDummyCommands(File dir) {
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
            'systemctl',
            'which',
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'k8s-master', serviceFactory
        services.putAvailableScriptService 'k8s-master/debian/8', scriptFactory
        services.putAvailableService 'ssh', sshFactory
    }

    List getAdditionalModules() {
        [
            new SshModule(),
            new SshPreModule(),
            new K8sMasterModule(),
            new K8sMaster_1_5_Debian_8_Module(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new CmdImplModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new FactsModule(),
            new TemplateModule(),
            new TokensTemplateModule(),
            new ResourcesModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                }
            }
        ]
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
