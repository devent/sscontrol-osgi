package com.anrisoftware.sscontrol.utils.fabric.test.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Test

import io.fabric8.kubernetes.api.KubernetesHelper
import io.fabric8.kubernetes.api.model.KubernetesResource
import io.fabric8.kubernetes.api.model.Namespace


/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class KubectlTest extends AbstractFabricTest {

    @Test
    void "load yamls"() {
        Namespace ns = KubernetesHelper.loadYaml KubectlTest.class.getResource("cloud-test-ns_yaml.txt").openStream(), KubernetesResource.class
        //server.client.services().create(serv)
        println ns.kind
        server.client.namespaces().create(ns)
    }
}
