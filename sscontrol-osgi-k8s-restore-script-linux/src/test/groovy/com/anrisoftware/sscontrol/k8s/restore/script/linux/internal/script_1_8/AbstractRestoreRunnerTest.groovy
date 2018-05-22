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
package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static java.nio.charset.StandardCharsets.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.Before

import com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImpl.RestoreImplFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunnerModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.runner.test.external.AbstractRunnerTestBase
import com.anrisoftware.sscontrol.ssh.script.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.script.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class AbstractRestoreRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractRestoreRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractRestoreRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractRestoreRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractRestoreRunnerTest.class.getResource('cert_key.txt')

    static final Map andreaLocalCerts = [
        worker: [
            ca: AbstractRestoreRunnerTest.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: AbstractRestoreRunnerTest.class.getResource('andrea_local_node_0_robobee_test_cert.pem'),
            key: AbstractRestoreRunnerTest.class.getResource('andrea_local_node_0_test_key_insecure.pem'),
        ],
    ]

    static final def KUBECTL_COMMAND = { IOUtils.toString(AbstractRestoreRunnerTest.class.getResource('kubectl_command.txt').openStream(), UTF_8) }

    static final URL rsyncKey = AbstractRestoreRunnerTest.class.getResource("robobee")

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory sshLinuxFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    RestoreImplFactory restoreFactory

    @Inject
    RestoreLinuxFactory restoreLinuxFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', sshLinuxFactory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'restore', restoreFactory
        services.putAvailableScriptService 'restore/linux/0', restoreLinuxFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll RestoreTestModules.getAdditionalModules()
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
