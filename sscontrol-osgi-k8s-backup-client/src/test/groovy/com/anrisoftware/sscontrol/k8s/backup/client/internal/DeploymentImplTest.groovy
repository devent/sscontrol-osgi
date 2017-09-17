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
package com.anrisoftware.sscontrol.k8s.backup.client.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Test

import io.fabric8.kubernetes.api.model.extensions.Deployment


/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class DeploymentImplTest extends AbstractTestDeployment {

    @Test
    void "get deployment"() {
        def mariadbDeploy = createMariadbDeploy()
        def deployment = createDeployment()
        def dop = deployment.getDeployment 'test', 'mariadb'
        Deployment d = dop.get()
        assert dop.namespace == 'test'
        assert dop.name == 'mariadb'
        assert d.spec.replicas == 1
    }

    //@Test
    void "scale deployment"() {
        def mariadbDeploy = createMariadbDeploy()
        def deployment = createDeployment()
        def dop = deployment.getDeployment 'test', 'mariadb'
        deployment.scaleDeployment dop, 0
        Deployment d = dop.get()
        assert dop.namespace == 'test'
        assert dop.name == 'mariadb'
        assert d.spec.replicas == 0
    }
}
