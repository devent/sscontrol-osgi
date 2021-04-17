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
package com.anrisoftware.sscontrol.k8smaster.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8smaster.service.external.K8sMaster
import com.anrisoftware.sscontrol.k8smaster.service.internal.K8sMasterImpl.K8sMasterImplFactory
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
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
class K8sMasterScriptTest {

    @Inject
    K8sMasterImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "cluster args"() {
        def test = [
            name: 'cluster args',
            input: """
service "k8s-master" with {
    cluster advertise: '192.168.0.1', hostname: '192.168.0.1', service: '10.3.0.0/24', pod: '10.2.0.0/16', api: 'http://localhost:8080'
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.advertiseAddress == '192.168.0.1'
                assert s.cluster.serviceRange == '10.3.0.0/24'
                assert s.cluster.podRange == '10.2.0.0/16'
                assert s.cluster.apiServers.size() == 1
                assert s.cluster.apiServers[0] == 'http://localhost:8080'
            },
        ]
        doTest test
    }

    @Test
    void "nodes_args"() {
        def test = [
            name: 'nodes_args',
            input: """
service "k8s-master", nodes: "node-0" with {
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0]
                assert s.nodes.size() == 1
                assert s.nodes[0] == 'node-0'
            },
        ]
        doTest test
    }

    @Test
    void "bind"() {
        def test = [
            name: 'bind',
            input: """
service "k8s-master" with {
    bind insecure: "127.0.0.1", secure: "0.0.0.0", insecurePort: 8080, port: 443
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.binding.insecureAddress == "127.0.0.1"
                assert s.binding.secureAddress == "0.0.0.0"
                assert s.binding.insecurePort == 8080
                assert s.binding.port == 443
            },
        ]
        doTest test
    }

    @Test
    void "account"() {
        def test = [
            name: 'account',
            input: """
service "k8s-master" with {
    account ca: "ca.pem", cert: "cert.pem", key: "key.pem"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.account.tls.ca.toString() =~ /.*ca\.pem/
                assert s.account.tls.cert.toString() =~ /.*cert\.pem/
                assert s.account.tls.key.toString() =~ /.*key\.pem/
            },
        ]
        doTest test
    }

    @Test
    void "allow privileged"() {
        def test = [
            name: 'allow privileged',
            input: """
service "k8s-master" with {
    privileged true
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.allowPrivileged == true
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin target"() {
        def test = [
            name: 'ectd plugin target',
            input: """
service "k8s-master" with {
    plugin "etcd", endpoint: "infra-0"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                assert s.plugins['etcd'].name == 'etcd'
                assert s.plugins['etcd'].endpoints.size() == 1
                assert s.plugins['etcd'].endpoints[0] == 'infra-0'
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin targets"() {
        def test = [
            name: 'ectd plugin targets',
            input: """
service "k8s-master" with {
    plugin "etcd", endpoint: "infra-0,infra-1"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                assert s.plugins['etcd'].name == 'etcd'
                assert s.plugins['etcd'].endpoints.size() == 2
                assert s.plugins['etcd'].endpoints[0] == 'infra-0'
                assert s.plugins['etcd'].endpoints[1] == 'infra-1'
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin address"() {
        def test = [
            name: 'ectd plugin address',
            input: """
service "k8s-master" with {
    plugin "etcd", endpoint: "http://etcd-0:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                assert s.plugins['etcd'].name == 'etcd'
                assert s.plugins['etcd'].endpoints.size() == 1
                assert s.plugins['etcd'].endpoints[0] == 'http://etcd-0:2379'
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin addresses"() {
        def test = [
            name: 'ectd plugin addresses',
            input: """
service "k8s-master" with {
    plugin "etcd", endpoint: "http://etcd-0:2379,http://etcd-1:2379"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                assert s.plugins['etcd'].name == 'etcd'
                assert s.plugins['etcd'].endpoints.size() == 2
                assert s.plugins['etcd'].endpoints[0] == 'http://etcd-0:2379'
                assert s.plugins['etcd'].endpoints[1] == 'http://etcd-1:2379'
            },
        ]
        doTest test
    }

    @Test
    void "auth"() {
        def test = [
            name: 'auth',
            input: '''
service "k8s-master" with {
    tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    authentication "cert", ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    authentication "basic", file: "some_file"
    authentication type: "basic", tokens: """\
token,user,uid,"group1,group2,group3"
"""
    authorization "allow"
    authorization "deny"
    authorization "abac", file: "policy_file.json"
    authorization mode: "abac", abac: """\
{"apiVersion": "abac.authorization.kubernetes.io/v1beta1", "kind": "Policy", "spec": {"user": "alice", "namespace": "*", "resource": "*", "apiGroup": "*"}}
"""
    admission << "AlwaysAdmit,ServiceAccount"
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 0
                assert s.tls.ca.toString() =~ /.*ca\.pem/
                assert s.tls.cert.toString() =~ /.*cert\.pem/
                assert s.tls.key.toString() =~ /.*key\.pem/
                assert s.authentications.size() == 3
                int k = -1
                assert s.authentications[++k].type == 'cert'
                assert s.authentications[k].ca.toString() =~ /.*ca\.pem/
                assert s.authentications[k].cert.toString() =~ /.*cert\.pem/
                assert s.authentications[k].key.toString() =~ /.*key\.pem/
                assert s.authentications[++k].type == 'basic'
                assert s.authentications[k].file.toString() =~ /.*some_file/
                assert s.authentications[++k].type == 'basic'
                assert s.authentications[k].file == null
                assert s.authentications[k].tokens =~ /token,user.*/
                assert s.authorizations.size() == 4
                k = -1
                assert s.authorizations[++k].mode == 'allow'
                assert s.authorizations[++k].mode == 'deny'
                assert s.authorizations[++k].mode == 'abac'
                assert s.authorizations[++k].mode == 'abac'
                assert s.admissions.size() == 2
                k = -1
                assert s.admissions[++k] == 'AlwaysAdmit'
                assert s.admissions[++k] == 'ServiceAccount'
            },
        ]
        doTest test
    }

    @Test
    void "kubelet"() {
        def test = [
            name: 'kubelet',
            input: '''
service "k8s-master" with {
    kubelet.with {
        tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
        preferred types: "InternalIP,Hostname,ExternalIP"
        bind port: 10250
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.kubelet.tls.ca.toString() =~ /.*ca\.pem/
                assert s.kubelet.tls.cert.toString() =~ /.*cert\.pem/
                assert s.kubelet.tls.key.toString() =~ /.*key\.pem/
                assert s.kubelet.preferredAddressTypes.size() == 3
                assert s.kubelet.port == 10250
                def k = -1
                assert s.kubelet.preferredAddressTypes[++k] == "InternalIP"
                assert s.kubelet.preferredAddressTypes[++k] == "Hostname"
                assert s.kubelet.preferredAddressTypes[++k] == "ExternalIP"
            },
        ]
        doTest test
    }

    @Test
    void "kubelet port arg"() {
        def test = [
            name: 'kubelet',
            input: '''
service "k8s-master" with {
    kubelet port: 10250 with {
        tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.kubelet.port == 10250
            },
        ]
        doTest test
    }

    @Test
    void "etcd tls"() {
        def test = [
            name: 'etcd tls',
            input: '''
service "k8s-master" with {
    plugin "etcd", endpoint: "infra-0" with {
        tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.plugins.size() == 1
                def etcd = s.plugins['etcd']
                assert etcd.name == 'etcd'
                assert etcd.endpoints.size() == 1
                assert etcd.endpoints[0] == 'infra-0'
                assert etcd.tls.ca.toString() =~ /.*ca\.pem/
                assert etcd.tls.cert.toString() =~ /.*cert\.pem/
                assert etcd.tls.key.toString() =~ /.*key\.pem/
            },
        ]
        doTest test
    }

    @Test
    void "labels"() {
        def test = [
            name: 'labels',
            input: '''
service "k8s-master" with {
    label << "muellerpublic.de/usage=apps"
    label key: "muellerpublic.de/some", value: "foo"
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
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
            input: '''
service "k8s-master" with {
    taint << "node.alpha.kubernetes.io/ismaster=:NoSchedule"
    taint << "dedicated=mail:NoSchedule"
    taint key: "extra", value: "foo", effect: "aaa"
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.serviceRange == null
                assert s.taints.size() == 3
                assert s.taints['node.alpha.kubernetes.io/ismaster'].value == ''
                assert s.taints['node.alpha.kubernetes.io/ismaster'].effect == 'NoSchedule'
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
        services.putAvailableService 'k8s-master', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new K8sModule(),
                new K8sMasterModule(),
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
