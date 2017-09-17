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
package com.anrisoftware.sscontrol.utils.fabric.test.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Test

import io.fabric8.kubernetes.api.KubernetesHelper
import io.fabric8.kubernetes.api.model.KubernetesResource
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim
import io.fabric8.kubernetes.api.model.Secret
import io.fabric8.kubernetes.api.model.StatusBuilder
import io.fabric8.kubernetes.api.model.extensions.Deployment
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder


/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class KubectlTest extends AbstractFabricTest {

    @Test
    void "load yaml namespace"() {
        server.expect().post().withPath("/api/v1/namespaces").andReturn(201, new StatusBuilder().build()).always()
        Namespace ns = KubernetesHelper.loadYaml KubectlTest.class.getResource("cloud-test-ns_yaml.txt").openStream(), KubernetesResource.class
        server.client.namespaces().create(ns)
        assert ns.metadata.name == 'cloud-test'
        assert ns.metadata.labels.group == 'cloud-test'
    }

    @Test
    void "load replace yaml namespace"() {
        server.expect().post().withPath("/api/v1/namespaces").andReturn(201, new StatusBuilder().build()).always()
        Namespace ns = KubernetesHelper.loadYaml KubectlTest.class.getResource("cloud-test-ns_yaml.txt").openStream(), KubernetesResource.class
        server.client.namespaces().createOrReplace(ns)
        assert ns.metadata.name == 'cloud-test'
        assert ns.metadata.labels.group == 'cloud-test'
    }

    @Test
    void "load yaml pvc storageclass"() {
        server.expect().post().withPath("/api/v1/namespaces/test/persistentvolumeclaims").andReturn(201, new StatusBuilder().build()).always()
        PersistentVolumeClaim pvc = KubernetesHelper.loadYaml KubectlTest.class.getResource("cloud-test-pvc_storageclass_yaml.txt").openStream(), KubernetesResource.class
        server.client.persistentVolumeClaims().create(pvc)
        assert pvc.metadata.name == 'myclaim'
        assert pvc.spec.additionalProperties.storageClassName == 'slow'
    }

    @Test
    void "load yaml deploy"() {
        server.expect().post().withPath("/apis/extensions/v1beta1/namespaces/test/deployments").andReturn(201, new StatusBuilder().build()).always()
        Deployment d = KubernetesHelper.loadYaml KubectlTest.class.getResource("cloud-test-mariadb-deploy_yaml.txt").openStream(), KubernetesResource.class
        server.client.extensions().deployments().create(d)
        assert d.metadata.name == 'mariadb'
        assert d.spec.template.spec.additionalProperties.affinity.nodeAffinity.preferredDuringSchedulingIgnoredDuringExecution.size() == 1
        assert d.spec.template.spec.additionalProperties.tolerations.size() == 1
    }

    @Test
    void "patch yaml deploy"() {
        server.expect().get().withPath("/apis/extensions/v1beta1/namespaces/test/deployments").andReturn(201, new StatusBuilder().build()).always()
        server.expect().withPath("/apis/extensions/v1beta1/namespaces/test/deployments/mariadb")
                .andReturn(200, new DeploymentBuilder().withNewMetadata().withName("mariadb").endMetadata().build())
                .always()
        Deployment d = KubernetesHelper.loadYaml KubectlTest.class.getResource("cloud-test-mariadb-deploy_yaml.txt").openStream(), KubernetesResource.class
        server.client.extensions().deployments().patch(d)
        assert d.metadata.name == 'mariadb'
        assert d.spec.template.spec.additionalProperties.affinity.nodeAffinity.preferredDuringSchedulingIgnoredDuringExecution.size() == 1
        assert d.spec.template.spec.additionalProperties.tolerations.size() == 1
    }

    @Test
    void "load yamls multiple"() {
        server.expect().post().withPath("/api/v1/namespaces/test/secrets").andReturn(201, new StatusBuilder().build()).always()
        server.expect().post().withPath("/api/v1/namespaces/test/persistentvolumeclaims").andReturn(201, new StatusBuilder().build()).always()
        server.expect().post().withPath("/apis/extensions/v1beta1/namespaces/test/deployments").andReturn(201, new StatusBuilder().build()).always()
        def om = KubernetesHelper.createYamlObjectMapper()
        def jp = om.factory.createParser(KubectlTest.class.getResource("cloud-test-multiple_yaml.txt").openStream())
        def r = om.readValues jp, KubernetesResource.class
        def list = r.inject([]) { v, i -> v << i }
        assert list.size() == 3
        assert list[0] instanceof Secret
        server.client.secrets().create list[0]
        assert list[1] instanceof PersistentVolumeClaim
        server.client.persistentVolumeClaims().create list[1]
        assert list[2] instanceof Deployment
        server.client.extensions().deployments().create list[2]
    }
}
