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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.etcd_3_2

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.etcd.service.internal.EtcdImpl.EtcdImplFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunnerModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.runner.test.external.AbstractRunnerTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshPreScriptImpl.SshPreScriptImplFactory
import com.anrisoftware.sscontrol.ssh.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class AbstractEtcdRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractEtcdRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractEtcdRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractEtcdRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractEtcdRunnerTest.class.getResource('cert_key.txt')

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractEtcdRunnerTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        etcd: [
            [cert: AbstractEtcdRunnerTest.class.getResource('andrea_local_etcd_etcd_0_robobee_test_cert.pem'),
                key: AbstractEtcdRunnerTest.class.getResource('andrea_local_etcd_etcd_0_robobee_test_key_insecure.pem'),
            ],
            [cert: AbstractEtcdRunnerTest.class.getResource('andrea_local_etcd_etcd_1_robobee_test_cert.pem'),
                key: AbstractEtcdRunnerTest.class.getResource('andrea_local_etcd_etcd_1_robobee_test_key_insecure.pem'),
            ],
        ],
    ]

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    SshPreScriptImplFactory sshPreFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    EtcdImplFactory serviceFactory

    @Inject
    Etcd_3_2_Debian_9_Factory scriptFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailablePreService 'ssh', sshPreFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'etcd', serviceFactory
        services.putAvailableScriptService 'etcd/debian/9', scriptFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll EtcdModules.getAdditionalModules()
        modules
    }


    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
