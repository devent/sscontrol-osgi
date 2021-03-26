/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8s.backup.script.linux.internal.script_1_13

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static java.nio.charset.StandardCharsets.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.BeforeEach

import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImpl.BackupImplFactory
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
abstract class AbstractBackupRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractBackupRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractBackupRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractBackupRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractBackupRunnerTest.class.getResource('cert_key.txt')

    static final URL rsyncKey = AbstractBackupRunnerTest.class.getResource("robobee")

    static final def KUBECTL_COMMAND = {
        IOUtils.toString(AbstractBackupRunnerTest.class.getResource('kubectl_command.txt').openStream(), UTF_8)
    }

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    BackupImplFactory backupFactory

    @Inject
    BackupLinuxFactory backupLinuxFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'backup', backupFactory
        services.putAvailableScriptService 'backup/linux/0', backupLinuxFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll BackupTestModules.getAdditionalModules()
        modules
    }


    @BeforeEach
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
