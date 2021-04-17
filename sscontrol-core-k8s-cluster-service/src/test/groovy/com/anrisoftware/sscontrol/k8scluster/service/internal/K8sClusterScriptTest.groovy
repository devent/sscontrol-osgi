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
package com.anrisoftware.sscontrol.k8scluster.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.sscontrol.k8scluster.service.external.K8sCluster
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.ssh.external.Ssh

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class K8sClusterScriptTest {

    @Inject
    K8sClusterFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "explicit target, default cluster and context"() {
        def test = [
            input: """
service "k8s-cluster" with {
    target name: 'default'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-cluster').size() == 1
                K8sCluster s = services.getServices('k8s-cluster')[0] as K8sCluster
                assert s.group == 'default'
                assert s.targets.size() == 0
                assert s.cluster.name == null
                assert s.context.name == null
            },
        ]
        doTest test
    }

    @Test
    void "explicit statements"() {
        def test = [
            input: """
service "k8s-cluster", target: 'default' with {
    cluster name: 'default-context'
    context name: 'default-system'
    credentials type: 'cert', port: 443, name: 'default-admin', ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-cluster').size() == 1
                K8sCluster s = services.getServices('k8s-cluster')[0] as K8sCluster
                assert s.group == 'default'
                assert s.cluster.name == 'default-context'
                assert s.context.name == 'default-system'
                assert s.credentials.size() == 1
                assert s.credentials[0].type == 'cert'
                assert s.credentials[0].name == 'default-admin'
                assert s.credentials[0].port == 443
                assert s.credentials[0].tls.ca.toString() =~ /.*ca\.pem/
                assert s.credentials[0].tls.cert.toString() =~ /.*cert\.pem/
                assert s.credentials[0].tls.key.toString() =~ /.*key\.pem/
            },
        ]
        doTest test
    }

    @Test
    void "statements as arguments"() {
        def test = [
            name: 'statements_args',
            input: """
service "k8s-cluster", cluster: 'default-cluster', context: 'default-system' with {
    credentials 'cert', name: 'default-admin', ca: 'ca.pem', cert: 'cert.pem', key: 'key.pem'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-cluster').size() == 1
                K8sCluster s = services.getServices('k8s-cluster')[0] as K8sCluster
                assert s.group == 'default'
                assert s.cluster.name == 'default-cluster'
                assert s.context.name == 'default-system'
                assert s.credentials.size() == 1
                assert s.credentials[0].type == 'cert'
                assert s.credentials[0].name == 'default-admin'
                assert s.credentials[0].tls.ca.toString() =~ /.*ca\.pem/
                assert s.credentials[0].tls.cert.toString() =~ /.*cert\.pem/
                assert s.credentials[0].tls.key.toString() =~ /.*key\.pem/
            },
        ]
        doTest test
    }

    @Test
    void "only with defaults"() {
        def test = [
            name: 'defaults',
            input: """
service "k8s-cluster"
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-cluster').size() == 1
                K8sCluster s = services.getServices('k8s-cluster')[0] as K8sCluster
                assert s.group == 'default'
                assert s.cluster.name == null
                assert s.context.name == null
                assert s.credentials.size() == 0
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'k8s-cluster', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        K8sClusterModules.createInjector().injectMembers(this)
    }
}
