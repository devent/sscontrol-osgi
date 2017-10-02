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
package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8

import javax.inject.Inject

import org.junit.Test

import com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8.KubectlClientFactory
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.utils.fabric.test.external.AbstractFabricTest

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class KubectlClientTest extends AbstractFabricTest {

    @Inject
    KubectlClientFactory kubectlFactory

    @Override
    List getAdditionalModules() {
        KubectlClientTestModules.modules
    }

    @Test
    void "createClient"() {
        createTestNode()
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost()
        def kubectl = kubectlFactory.create cluster, script
        kubectl.createClient()
    }
}
