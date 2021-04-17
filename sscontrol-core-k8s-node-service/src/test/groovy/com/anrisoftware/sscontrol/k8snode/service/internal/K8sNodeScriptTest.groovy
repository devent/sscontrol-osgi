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
package com.anrisoftware.sscontrol.k8snode.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8snode.service.external.K8sNode
import com.anrisoftware.sscontrol.k8snode.service.internal.K8sNodeImpl.K8sNodeImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScriptModule
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.ssh.service.internal.SshModule
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
class K8sNodeScriptTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    RobobeeScriptFactory robobeeScriptFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    K8sNodeImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "cluster_join_statement"() {
        def test = [
            name: 'cluster_join_statement',
            script: """
service "k8s-node" with {
    cluster join: 'kubeadm join --token 34f578.e9676c9fc49544bb 192.168.56.200:443 --discovery-token-ca-cert-hash sha256:7501bc596d3dce2f88ece232d3454876293bea94884bb19f90f2ebc6824e845f'
}
""",
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0] as K8sNode
                assert s.cluster.joinCommand == 'kubeadm join --token 34f578.e9676c9fc49544bb 192.168.56.200:443 --discovery-token-ca-cert-hash sha256:7501bc596d3dce2f88ece232d3454876293bea94884bb19f90f2ebc6824e845f'
            },
        ]
        doTest test
    }

    @Test
    void "cluster_join_service"() {
        def test = [
            name: 'cluster_join_service',
            script: """
service "k8s-node", join: 'kubeadm join --token 34f578.e9676c9fc49544bb 192.168.56.200:443 --discovery-token-ca-cert-hash sha256:7501bc596d3dce2f88ece232d3454876293bea94884bb19f90f2ebc6824e845f' with {
}
""",
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0] as K8sNode
                assert s.cluster.joinCommand == 'kubeadm join --token 34f578.e9676c9fc49544bb 192.168.56.200:443 --discovery-token-ca-cert-hash sha256:7501bc596d3dce2f88ece232d3454876293bea94884bb19f90f2ebc6824e845f'
            },
        ]
        doTest test
    }

    @Test
    void "labels"() {
        def test = [
            name: 'labels',
            script: '''
service "k8s-node" with {
    label << "muellerpublic.de/usage=apps"
    label key: "muellerpublic.de/some", value: "foo"
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0]
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.labels.size() == 2
                assert s.labels['muellerpublic.de/usage'].value == 'apps'
                assert s.labels['muellerpublic.de/some'].value == 'foo'
            },
        ]
        doTest test
    }

    @Test
    void "taints"() {
        def test = [
            name: 'taints',
            script: '''
service "k8s-node" with {
    taint << "node.alpha.kubernetes.io/ismaster=:NoSchedule"
    taint << "dedicated=mail:NoSchedule"
    taint key: "extra", value: "foo", effect: "aaa"
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('k8s-node').size() == 1
                K8sNode s = services.getServices('k8s-node')[0]
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.taints.size() == 3
                assert s.taints['node.alpha.kubernetes.io/ismaster'].value == ''
                assert s.taints['node.alpha.kubernetes.io/ismaster'].effect == 'NoSchedule'
                assert s.taints['dedicated'].value == 'mail'
                assert s.taints['dedicated'].effect == 'NoSchedule'
                assert s.taints['extra'].value == 'foo'
                assert s.taints['extra'].effect == 'aaa'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'k8s-node', serviceFactory
        robobeeScriptFactory.create folder.newFile(), test.script, test.scriptVars, services call()
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new SshModule(),
                new K8sModule(),
                new K8sNodeModule(),
                new RobobeeScriptModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
