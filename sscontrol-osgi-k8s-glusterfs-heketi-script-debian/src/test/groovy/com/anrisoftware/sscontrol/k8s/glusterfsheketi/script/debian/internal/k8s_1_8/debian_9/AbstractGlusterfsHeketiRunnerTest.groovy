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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.script.debian.internal.k8s_1_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8s.fromrepository.script.linux.internal.script_1_7.FromRepositoryLinuxFactory
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImpl.GlusterfsHeketiImplFactory
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8.K8sClusterLinuxFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux.DockerRegistryLinuxFactory
import com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImpl.DockerRegistryImplFactory
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9.GitRepoDebianFactory
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunnerModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.runner.test.external.AbstractRunnerTestBase
import com.anrisoftware.sscontrol.shell.internal.ShellImpl.ShellImplFactory
import com.anrisoftware.sscontrol.shell.linux.external.Shell_Linux_Factory
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

    static final Map certs = [
        worker: [
            ca: AbstractGlusterfsHeketiRunnerTest.class.getResource('cert_ca.txt'),
            cert: AbstractGlusterfsHeketiRunnerTest.class.getResource('cert_cert.txt'),
            key: AbstractGlusterfsHeketiRunnerTest.class.getResource('cert_key.txt'),
        ],
    ]

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
    DockerRegistryImplFactory dockerRegistryFactory

    @Inject
    DockerRegistryLinuxFactory dockerRegistryLinuxFactory

    @Inject
    FromRepositoryImplFactory fromRepositoryFactory

    @Inject
    FromRepositoryLinuxFactory fromRepositoryLinuxFactory

    @Inject
    GlusterfsHeketiImplFactory glusterfsHeketiFactory

    @Inject
    GlusterfsHeketiDebianFactory glusterfsHeketiDebianFactory

    @Inject
    ShellImplFactory shellFactory

    @Inject
    Shell_Linux_Factory shellLinuxFactory

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
        services.putAvailableService 'registry-docker', dockerRegistryFactory
        services.putAvailableScriptService 'registry-docker/linux/0', dockerRegistryLinuxFactory
        services.putAvailableService 'from-repository', fromRepositoryFactory
        services.putAvailableScriptService 'from-repository/linux/0', fromRepositoryLinuxFactory
        services.putAvailableService 'glusterfs-heketi', glusterfsHeketiFactory
        services.putAvailableScriptService 'glusterfs-heketi/debian/9', glusterfsHeketiDebianFactory
        services.putAvailableService 'shell', shellFactory
        services.putAvailableScriptService 'shell/linux/0', shellLinuxFactory
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
