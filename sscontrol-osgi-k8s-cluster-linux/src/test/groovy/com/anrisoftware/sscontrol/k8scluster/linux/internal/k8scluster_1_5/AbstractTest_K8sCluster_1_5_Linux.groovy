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
package com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.command.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.command.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.command.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.st.StModule
import com.anrisoftware.sscontrol.command.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterModule
import com.anrisoftware.sscontrol.k8skubectl.linux.external.Kubectl_1_6_Linux_Module
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTest_K8sCluster_1_5_Linux extends AbstractScriptTestBase {

    static final URL certCaPem = AbstractTest_K8sCluster_1_5_Linux.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractTest_K8sCluster_1_5_Linux.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractTest_K8sCluster_1_5_Linux.class.getResource('cert_key.txt')

    static final URL kubectl = AbstractTest_K8sCluster_1_5_Linux.class.getResource('kubectl.txt')

    @Inject
    SshImplFactory sshFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    K8sClusterFactory serviceFactory

    @Inject
    K8sCluster_1_5_Linux_Factory scriptFactory

    String getServiceName() {
        'k8s-cluster'
    }

    String getScriptServiceName() {
        'k8s-cluster/linux/0'
    }

    void createDummyCommands(File dir) {
        createIdCommand dir
        createDebianJessieCatCommand dir
        createCommand exit1Command, dir, 'dpkg'
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
            'docker',
            'cat',
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'k8s-cluster', serviceFactory
        services.putAvailableScriptService 'k8s-cluster/linux/0', scriptFactory
    }

    List getAdditionalModules() {
        [
            new SshModule(),
            new K8sModule(),
            new K8sClusterModule(),
            new Kubectl_1_6_Linux_Module(),
            new K8sCluster_1_5_Linux_Module(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new StModule(),
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
            new SystemNameMappingsModule(),
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
