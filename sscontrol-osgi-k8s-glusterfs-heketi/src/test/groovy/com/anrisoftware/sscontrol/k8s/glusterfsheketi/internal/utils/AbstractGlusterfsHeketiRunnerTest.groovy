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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.utils

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5.FromRepository_1_5_Factory
import com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.service.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6.GlusterfsHeketi_1_6_Factory
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.service.GlusterfsHeketiImpl.GlusterfsHeketiImplFactory
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Factory
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory
import com.anrisoftware.sscontrol.registry.docker.service.internal.debian_8.DockerRegistry_Debian_8_Factory
import com.anrisoftware.sscontrol.repo.git.linux.internal.debian_8.GitRepo_Debian_8_Factory
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
abstract class AbstractGlusterfsHeketiRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractGlusterfsHeketiRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractGlusterfsHeketiRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractGlusterfsHeketiRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractGlusterfsHeketiRunnerTest.class.getResource('cert_key.txt')

    static final Map andreaLocalCerts = [
        worker: [
            ca: AbstractGlusterfsHeketiRunnerTest.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: AbstractGlusterfsHeketiRunnerTest.class.getResource('andrea_local_node_0_robobee_test_cert.pem'),
            key: AbstractGlusterfsHeketiRunnerTest.class.getResource('andrea_local_node_0_test_key_insecure.pem'),
        ],
    ]

    static final URL wordpressZip = AbstractGlusterfsHeketiRunnerTest.class.getResource('wordpress-app.zip')

    static final URL wordpressStZip = AbstractGlusterfsHeketiRunnerTest.class.getResource('wordpress-app-st.zip')

    static final URL wordpressStgZip = AbstractGlusterfsHeketiRunnerTest.class.getResource('wordpress-app-stg.zip')

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    K8sCluster_1_5_Linux_Factory cluster_1_5_Factory

    @Inject
    GitRepoImplFactory gitFactory

    @Inject
    GitRepo_Debian_8_Factory gitScriptFactory

    @Inject
    DockerRegistryImplFactory dockerFactory

    @Inject
    DockerRegistry_Debian_8_Factory dockerScriptFactory

    @Inject
    FromRepositoryImplFactory fromRepositoryFactory

    @Inject
    FromRepository_1_5_Factory fromRepository_1_5_Factory

    @Inject
    GlusterfsHeketiImplFactory glusterfsHeketiFactory

    @Inject
    GlusterfsHeketi_1_6_Factory glusterfsHeketi_1_6_Factory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableScriptService 'k8s/cluster/linux/0', cluster_1_5_Factory
        services.putAvailableService 'repo-git', gitFactory
        services.putAvailableScriptService 'repo-git/debian/8', gitScriptFactory
        services.putAvailableService 'registry-docker', dockerFactory
        services.putAvailableScriptService 'registry-docker/debian/8', dockerScriptFactory
        services.putAvailableService 'from-repository', fromRepositoryFactory
        services.putAvailableScriptService 'from-repository/linux/0', fromRepository_1_5_Factory
        services.putAvailableService 'glusterfs-heketi', glusterfsHeketiFactory
        services.putAvailableScriptService 'glusterfs-heketi/linux/0', glusterfsHeketi_1_6_Factory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll GlusterfsHeketiTestModules.getAdditionalModules()
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
