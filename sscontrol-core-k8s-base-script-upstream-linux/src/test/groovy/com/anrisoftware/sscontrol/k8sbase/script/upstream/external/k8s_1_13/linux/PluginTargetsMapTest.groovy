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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_13.linux

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach

import com.anrisoftware.sscontrol.k8sbase.base.service.internal.EtcdPluginImpl.EtcdPluginImplFactory
import com.google.inject.Guice

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class PluginTargetsMapTest {

    @Inject
    PluginTargetsMapFactory pluginTargetsMapFactory

    @Inject
    EtcdPluginImplFactory etcdPluginFactory

    //@Test
    void "get etcd"() {
        def map = [
            "etcd": etcdPluginFactory.create([target: 'etcd0', protocol: 'http', port: 2379])
        ]
        etcdPluginFactory
    }

    @BeforeEach
    void injectDependencies() {
        Guice.createInjector(
                new K8sUpstreamModule()
                ).injectMembers(this)
    }
}
