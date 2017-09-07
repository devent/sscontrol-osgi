package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import org.junit.Test

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.utils.fabric.test.external.AbstractFabricTest

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class KubectlClientServerTest extends AbstractFabricTest {

    static final URL robobeeCa = KubectlClientServerTest.class.getResource('robobee_test_kube_ca.pem')

    static final URL robobeeCert = KubectlClientServerTest.class.getResource('robobee_test_kube_admin_cert.pem')

    static final URL robobeeKey = KubectlClientServerTest.class.getResource('robobee_test_kube_admin_key.pem')

    @Inject
    KubectlClientFactory kubectlFactory

    @Override
    List getAdditionalModules() {
        KubectlClientTestModules.modules
    }

    @Test
    void "waitNodeReady"() {
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost 'https://andrea-master.robobee-test.test:443', robobeeCa, robobeeCert, robobeeKey
        def kubectl = kubectlFactory.create script, cluster
        kubectl.waitNodeReady "robobeerun.com/node", "andrea-test-cluster", 10, TimeUnit.SECONDS
    }

    @Test
    void "applyLabelToNode"() {
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost 'https://andrea-master.robobee-test.test:443', robobeeCa, robobeeCert, robobeeKey
        def kubectl = kubectlFactory.create script, cluster
        def nodeLabel = "robobeerun.com/node"
        def nodeValue = "andrea-test-cluster"
        kubectl.applyLabelToNode nodeLabel, nodeValue, "test", "foo"
        try {
            def node = kubectl.getNode nodeLabel, nodeValue
            assert node.metadata.labels["test"] == "foo"
        } finally {
            kubectl.applyLabelToNode nodeLabel, nodeValue, "test-", null
            def node = kubectl.getNode nodeLabel, nodeValue
            assert !node.metadata.labels.containsKey("test")
        }
    }
}
