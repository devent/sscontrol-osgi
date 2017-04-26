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
package com.anrisoftware.sscontrol.k8smaster.debian.internal.k8smaster_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sModule
import com.anrisoftware.sscontrol.k8sbase.upstream.external.K8s_1_5_Upstream_Module
import com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterModule
import com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterImpl.K8sMasterImplFactory
import com.anrisoftware.sscontrol.k8smaster.upstream.external.K8sMaster_1_5_Upstream_Module
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.external.host.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTest_K8sMaster_Debian_8 extends AbstractScriptTestBase {

    static final URL certCaPem = K8sMaster_Debian_8_Test.class.getResource('cert_ca.txt')

    static final URL certCertPem = K8sMaster_Debian_8_Test.class.getResource('cert_cert.txt')

    static final URL certKeyPem = K8sMaster_Debian_8_Test.class.getResource('cert_key.txt')

    @Inject
    SshImplFactory sshFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    K8sMasterImplFactory serviceFactory

    @Inject
    K8sMaster_1_5_Debian_8_Factory scriptFactory

    String getServiceName() {
        'k8s-master'
    }

    String getScriptServiceName() {
        'k8s-master/debian/8'
    }

    void createDummyCommands(File dir) {
        createIdCommand dir
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'systemctl',
            'which',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'grep',
            'curl',
            'sleep',
            'kubectl',
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'k8s-master', serviceFactory
        services.putAvailableScriptService 'k8s-master/debian/8', scriptFactory
    }

    List getAdditionalModules() {
        [
            new SshModule(),
            new SshPreModule(),
            new K8sMasterModule(),
            new K8sModule(),
            new K8sMaster_1_5_Debian_8_Module(),
            new K8sMaster_1_5_Upstream_Module(),
            new K8s_1_5_Upstream_Module(),
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
            new TemplateResModule(),
            new TokensTemplateModule(),
            new ResourcesModule(),
            new TlsModule(),
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
