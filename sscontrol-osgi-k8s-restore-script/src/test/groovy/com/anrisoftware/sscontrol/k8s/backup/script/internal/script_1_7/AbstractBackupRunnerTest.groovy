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
package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static java.nio.charset.StandardCharsets.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.Before

import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImpl.BackupImplFactory
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Factory
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
abstract class AbstractBackupRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractBackupRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractBackupRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractBackupRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractBackupRunnerTest.class.getResource('cert_key.txt')

    static final Map andreaLocalCerts = [
        worker: [
            ca: AbstractBackupRunnerTest.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: AbstractBackupRunnerTest.class.getResource('andrea_local_node_0_robobee_test_cert.pem'),
            key: AbstractBackupRunnerTest.class.getResource('andrea_local_node_0_test_key_insecure.pem'),
        ],
    ]

    static final def KUBECTL_COMMAND = { IOUtils.toString(AbstractBackupRunnerTest.class.getResource('kubectl_command.txt').openStream(), UTF_8) }

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    SshPreScriptImplFactory sshPreFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    BackupImplFactory serviceFactory

    @Inject
    Backup_1_7_Factory scriptFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailablePreService 'ssh', sshPreFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'backup', serviceFactory
        services.putAvailableScriptService 'backup/linux/0', scriptFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll BackupTestModules.getAdditionalModules()
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