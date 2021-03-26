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
package com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_13;

import static com.anrisoftware.globalpom.utils.TestUtils.*

import java.nio.charset.StandardCharsets

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.BeforeEach

import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_13.K8sClusterLinuxFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux.DockerRegistryLinuxFactory
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9.GitRepoDebianFactory
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
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
abstract class AbstractFromRepositoryRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractFromRepositoryRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractFromRepositoryRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractFromRepositoryRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractFromRepositoryRunnerTest.class.getResource('cert_key.txt')

    static final def KUBECTL_COMMAND = { IOUtils.toString(AbstractFromRepositoryRunnerTest.class.getResource('kubectl_command.txt').openStream(), StandardCharsets.UTF_8) }

    static final def DOCKER_COMMAND = { IOUtils.toString(AbstractFromRepositoryRunnerTest.class.getResource('docker_command.txt').openStream(), StandardCharsets.UTF_8) }

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory sshLinuxFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    K8sClusterLinuxFactory clusterLinuxFactory

    @Inject
    GitRepoImplFactory gitFactory

    @Inject
    GitRepoDebianFactory gitDebianFactory

    @Inject
    DockerRegistryImplFactory dockerFactory

    @Inject
    DockerRegistryLinuxFactory dockerLinuxFactory

    @Inject
    FromRepositoryImplFactory fromRepositoryFactory

    @Inject
    FromRepositoryLinuxFactory fromRepositoryLinuxFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', sshLinuxFactory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableScriptService 'k8s/cluster/linux/0', clusterLinuxFactory
        services.putAvailableService 'repo-git', gitFactory
        services.putAvailableScriptService 'repo-git/debian/9', gitDebianFactory
        services.putAvailableService 'registry-docker', dockerFactory
        services.putAvailableScriptService 'registry-docker/linux/0', dockerLinuxFactory
        services.putAvailableService 'from-repository', fromRepositoryFactory
        services.putAvailableScriptService 'from-repository/linux/0', fromRepositoryLinuxFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll FromRepositoryTestModules.getAdditionalModules()
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
