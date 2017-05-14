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
package com.anrisoftware.sscontrol.flanneldocker.debian.internal.flanneldocker_0_7

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImpl.FlannelDockerImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFlannelDockerScriptTest extends AbstractScriptTestBase {

    static final URL certCaPem = AbstractFlannelDockerScriptTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractFlannelDockerScriptTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractFlannelDockerScriptTest.class.getResource('cert_key.txt')

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        cert: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_client_0_robobee_test_cert.pem'),
        key: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_client_0_robobee_test_key_insecure.pem'),
    ]

    @Inject
    SshImplFactory sshFactory

    @Inject
    FlannelDockerImplFactory serviceFactory

    @Inject
    FlannelDocker_0_7_Debian_8_Factory scriptFactory

    String getServiceName() {
        'flannel-docker'
    }

    String getScriptServiceName() {
        'flannel-docker/debian/8'
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
            'tar',
            'gpg',
            'curl',
            'grep',
            'mktemp',
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'flannel-docker', serviceFactory
        services.putAvailableScriptService 'flannel-docker/debian/8', scriptFactory
        services.putAvailableService 'ssh', sshFactory
    }

    List getAdditionalModules() {
        FlannelDockerModules.getAdditionalModules()
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
