package com.anrisoftware.sscontrol.utils.fabric.test.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Test

import io.fabric8.kubernetes.api.KubernetesHelper
import io.fabric8.kubernetes.api.model.KubernetesResource
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim
import io.fabric8.kubernetes.api.model.StatusBuilder
import io.fabric8.kubernetes.api.model.extensions.Deployment


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
    void "load yamls multiple"() {
        server.expect().post().withPath("/api/v1/namespaces/test/persistentvolumeclaims").andReturn(201, new StatusBuilder().build()).always()
        def om = KubernetesHelper.createYamlObjectMapper()
        def jp = om.factory.createParser(KubectlTest.class.getResource("cloud-test-multiple_yaml.txt").openStream())
        def r = om.readValues jp, KubernetesResource.class
        r.each { println it }
    }
}
