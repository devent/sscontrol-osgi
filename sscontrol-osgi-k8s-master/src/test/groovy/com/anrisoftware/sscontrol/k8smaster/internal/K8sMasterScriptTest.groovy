/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8smaster.external.K8sMaster
import com.anrisoftware.sscontrol.k8smaster.internal.K8sMasterImpl.K8sMasterImplFactory
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.types.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.external.Ssh
import com.anrisoftware.sscontrol.types.external.TargetsService
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule
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
    void "cluster range"() {
        def test = [
            name: 'cluster range',
            input: """
service "k8s-master" with {
    cluster range: "10.254.0.0/16"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.range == '10.254.0.0/16'
            },
        ]
        doTest test
    }

    @Test
    void "ectd plugin"() {
        def test = [
            name: 'ectd plugin',
            input: """
service "k8s-master" with {
    plugin "etcd", target: "infra0"
}
""",
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.range == null
                assert s.plugins.size() == 1
                assert s.plugins[0].name == 'etcd'
                assert s.plugins[0].target == 'infra0'
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
    kubelet.with {
        tls ca: "ca.pem", cert: "cert.pem", key: "key.pem"
        preferred types: "InternalIP,Hostname,ExternalIP"
    }
}
''',
            expected: { HostServices services ->
                assert services.getServices('k8s-master').size() == 1
                K8sMaster s = services.getServices('k8s-master')[0] as K8sMaster
                assert s.targets.size() == 0
                assert s.cluster.range == null
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
                assert s.kubelet.tls.ca.toString() =~ /.*ca\.pem/
                assert s.kubelet.tls.cert.toString() =~ /.*cert\.pem/
                assert s.kubelet.tls.key.toString() =~ /.*key\.pem/
                assert s.kubelet.nodeAddressTypes.size() == 3
                k = -1
                assert s.kubelet.nodeAddressTypes[++k] == "InternalIP"
                assert s.kubelet.nodeAddressTypes[++k] == "Hostname"
                assert s.kubelet.nodeAddressTypes[++k] == "ExternalIP"
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

    @Before
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new K8sMasterModule(),
                new K8sMasterPreModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                }).injectMembers(this)
    }
}
