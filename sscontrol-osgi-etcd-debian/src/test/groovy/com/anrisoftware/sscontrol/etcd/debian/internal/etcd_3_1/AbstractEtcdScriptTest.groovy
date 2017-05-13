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

import com.anrisoftware.sscontrol.etcd.internal.EtcdImpl.EtcdImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractEtcdScriptTest extends AbstractScriptTestBase {

    static final URL certCaPem = AbstractEtcdScriptTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractEtcdScriptTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractEtcdScriptTest.class.getResource('cert_key.txt')

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        etcd_0_cert: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_0_robobee_test_cert.pem'),
        etcd_0_key: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_0_robobee_test_key_insecure.pem'),
        etcd_1_cert: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_1_robobee_test_cert.pem'),
        etcd_1_key: AbstractEtcdScriptTest.class.getResource('andrea_local_etcd_etcd_1_robobee_test_key_insecure.pem'),
    ]

    static final URL grepActiveCommand = AbstractEtcdScriptTest.class.getResource('grep_active_command.txt')

    @Inject
    SshImplFactory sshFactory

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
        EtcdModules.getAdditionalModules()
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
