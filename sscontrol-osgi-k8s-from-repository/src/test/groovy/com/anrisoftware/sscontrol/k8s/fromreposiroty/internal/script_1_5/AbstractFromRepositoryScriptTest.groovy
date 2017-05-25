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
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.service.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterImpl.K8sClusterImplFactory
import com.anrisoftware.sscontrol.repo.git.linux.internal.debian_8.GitRepo_Debian_8_Factory
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFromRepositoryScriptTest extends AbstractScriptTestBase {

    static final URL kubectlCommand = AbstractFromRepositoryScriptTest.class.getResource('kubectl_command.txt')

    static final URL idRsa = AbstractFromRepositoryScriptTest.class.getResource('id_rsa.txt')

    static final URL certCaPem = AbstractFromRepositoryScriptTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractFromRepositoryScriptTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractFromRepositoryScriptTest.class.getResource('cert_key.txt')

    @Inject
    SshImplFactory sshFactory

    @Inject
    K8sClusterImplFactory clusterFactory

    @Inject
    GitRepoImplFactory gitFactory

    @Inject
    GitRepo_Debian_8_Factory gitScriptFactory

    @Inject
    FromRepositoryImplFactory serviceFactory

    @Inject
    FromRepository_1_5_Factory scriptFactory

    String getServiceName() {
        'from-repository'
    }

    String getScriptServiceName() {
        'from-repository/linux/0'
    }

    void createDummyCommands(File dir) {
        createIdCommand dir
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'mv',
            'basename',
            'git',
            'find'
        ]
        createCommand kubectlCommand, dir, 'kubectl'
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'repo-git', gitFactory
        services.putAvailableScriptService 'repo-git/debian/8', gitScriptFactory
        services.putAvailableService 'from-repository', serviceFactory
        services.putAvailableScriptService 'from-repository/linux/0', scriptFactory
    }

    List getAdditionalModules() {
        FromRepository_1_5_Modules.getAdditionalModules()
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
