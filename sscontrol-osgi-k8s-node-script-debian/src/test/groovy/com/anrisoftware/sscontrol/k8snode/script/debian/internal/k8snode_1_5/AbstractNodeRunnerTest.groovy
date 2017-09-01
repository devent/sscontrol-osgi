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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_7.K8sClusterLinuxFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8snode.service.internal.K8sNodeImpl.K8sNodeImplFactory
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
abstract class AbstractNodeRunnerTest extends AbstractRunnerTestBase {

    static final URL certCaPem = AbstractNodeRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractNodeRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractNodeRunnerTest.class.getResource('cert_key.txt')

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractNodeRunnerTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        cert: AbstractNodeRunnerTest.class.getResource('andrea_local_etcd_client_0_robobee_test_cert.pem'),
        key: AbstractNodeRunnerTest.class.getResource('andrea_local_etcd_client_0_robobee_test_key_insecure.pem'),
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
    K8sNodeImplFactory nodeFactory

    @Inject
    K8sNode_1_5_Debian_8_Factory nodeDebianFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', sshLinuxFactory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableScriptService 'k8s/cluster/linux/0', clusterLinuxFactory
        services.putAvailableService 'k8s-node', nodeFactory
        services.putAvailableScriptService 'k8s-node/debian/8', nodeDebianFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll NodeModules.getAdditionalModules()
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
