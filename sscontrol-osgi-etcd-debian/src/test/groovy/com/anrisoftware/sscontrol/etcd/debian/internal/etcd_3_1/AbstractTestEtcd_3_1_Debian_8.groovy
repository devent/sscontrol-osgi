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
package com.anrisoftware.sscontrol.etcd.debian.internal.etcd_3_1

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.etcd.internal.BindingModule
import com.anrisoftware.sscontrol.etcd.internal.EtcdModule
import com.anrisoftware.sscontrol.etcd.internal.EtcdImpl.EtcdImplFactory
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
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.google.inject.AbstractModule

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTestEtcd_3_1_Debian_8 extends AbstractScriptTestBase {

    static final URL certCaPem = AbstractTestEtcd_3_1_Debian_8.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractTestEtcd_3_1_Debian_8.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractTestEtcd_3_1_Debian_8.class.getResource('cert_key.txt')

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractTestEtcd_3_1_Debian_8.class.getResource('andrea_local_etcd_ca_cert.pem'),
        etcd_0_cert: AbstractTestEtcd_3_1_Debian_8.class.getResource('andrea_local_etcd_etcd_0_robobee_test_cert.pem'),
        etcd_0_key: AbstractTestEtcd_3_1_Debian_8.class.getResource('andrea_local_etcd_etcd_0_robobee_test_key_insecure.pem'),
        etcd_1_cert: AbstractTestEtcd_3_1_Debian_8.class.getResource('andrea_local_etcd_etcd_1_robobee_test_cert.pem'),
        etcd_1_key: AbstractTestEtcd_3_1_Debian_8.class.getResource('andrea_local_etcd_etcd_1_robobee_test_key_insecure.pem'),
    ]

    static final URL grepActiveCommand = AbstractTestEtcd_3_1_Debian_8.class.getResource('grep_active_command.txt')

    @Inject
    SshImplFactory sshFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    EtcdImplFactory serviceFactory

    @Inject
    Etcd_3_1_Debian_8_Factory scriptFactory

    String getServiceName() {
        'etcd'
    }

    String getScriptServiceName() {
        'etcd/debian/8'
    }

    void createDummyCommands(File dir) {
        createCommand grepActiveCommand, dir, 'grep'
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
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg'
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'etcd', serviceFactory
        services.putAvailableScriptService 'etcd/debian/8', scriptFactory
        services.putAvailableService 'ssh', sshFactory
    }

    List getAdditionalModules() {
        [
            new SshModule(),
            new SshPreModule(),
            new EtcdModule(),
            new BindingModule(),
            new Etcd_3_1_Debian_8_Module(),
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
