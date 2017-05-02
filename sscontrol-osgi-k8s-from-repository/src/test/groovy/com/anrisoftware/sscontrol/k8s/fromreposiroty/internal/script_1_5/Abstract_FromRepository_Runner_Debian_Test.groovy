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
package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.service.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterImpl.K8sClusterImplFactory
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Factory
import com.anrisoftware.sscontrol.repo.git.linux.internal.debian_8.GitRepo_Debian_8_Factory
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
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
abstract class Abstract_FromRepository_Runner_Debian_Test extends AbstractRunnerTestBase {

    static final URL kubectlCommand = Abstract_FromRepository_Runner_Debian_Test.class.getResource('kubectl_command.txt')

    static final URL certCaPem = Abstract_FromRepository_Runner_Debian_Test.class.getResource('cert_ca.txt')

    static final URL certCertPem = Abstract_FromRepository_Runner_Debian_Test.class.getResource('cert_cert.txt')

    static final URL certKeyPem = Abstract_FromRepository_Runner_Debian_Test.class.getResource('cert_key.txt')

    static final Map andreaLocalCerts = [
        worker: [
            ca: Abstract_FromRepository_Runner_Debian_Test.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: Abstract_FromRepository_Runner_Debian_Test.class.getResource('andrea_local_node_0_robobee_test_cert.pem'),
            key: Abstract_FromRepository_Runner_Debian_Test.class.getResource('andrea_local_node_0_test_key_insecure.pem'),
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
    K8sClusterImplFactory clusterFactory

    @Inject
    K8sCluster_1_5_Linux_Factory cluster_1_5_Factory

    @Inject
    GitRepoImplFactory gitFactory

    @Inject
    GitRepo_Debian_8_Factory gitScriptFactory

    @Inject
    FromRepositoryImplFactory serviceFactory

    @Inject
    FromRepository_1_5_Factory scriptFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailablePreService 'ssh', sshPreFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableScriptService 'k8s/cluster/linux/0', cluster_1_5_Factory
        services.putAvailableService 'git', gitFactory
        services.putAvailableScriptService 'git/debian/8', gitScriptFactory
        services.putAvailableService 'from-repository', serviceFactory
        services.putAvailableScriptService 'from-repository/linux/0', scriptFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll FromRepository_1_5_Modules.getAdditionalModules()
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
