package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import static org.junit.Assume.*

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import org.junit.Before
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
    KubeNodeClientFactory nodeClientFactory

    @Override
    List getAdditionalModules() {
        KubectlClientTestModules.modules
    }

    @Test
    void "waitNodeReady"() {
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost 'https://andrea-master.robobee-test.test:443', robobeeCa, robobeeCert, robobeeKey
        def kubectl = nodeClientFactory.create cluster, script
        kubectl.waitNodeReady "robobeerun.com/node", "andrea-test-cluster", 10, TimeUnit.SECONDS
    }

    @Test
    void "applyLabelToNode with key and value"() {
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost 'https://andrea-master.robobee-test.test:443', robobeeCa, robobeeCert, robobeeKey
        def nodeClient = nodeClientFactory.create cluster, script
        def nodeLabel = "robobeerun.com/node"
        def nodeValue = "andrea-test-cluster"
        nodeClient.applyLabelToNode nodeLabel, nodeValue, "test", "foo"
        try {
            def node = nodeClient.getNode nodeLabel, nodeValue
            assert node.node.metadata.labels["test"] == "foo"
        } finally {
            nodeClient.applyLabelToNode nodeLabel, nodeValue, "test-", null
            def node = nodeClient.getNode nodeLabel, nodeValue
            assert !node.node.metadata.labels.containsKey("test")
        }
    }

    @Test
    void "applyLabelToNode with key and no value"() {
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost 'https://andrea-master.robobee-test.test:443', robobeeCa, robobeeCert, robobeeKey
        def nodeClient = nodeClientFactory.create cluster, script
        def nodeLabel = "robobeerun.com/node"
        def nodeValue = "andrea-test-cluster"
        nodeClient.applyLabelToNode nodeLabel, nodeValue, "test", null
        try {
            def node = nodeClient.getNode nodeLabel, nodeValue
            assert node.node.metadata.labels["test"] == "null"
        } finally {
            nodeClient.applyLabelToNode nodeLabel, nodeValue, "test-", null
            def node = nodeClient.getNode nodeLabel, nodeValue
            assert !node.node.metadata.labels.containsKey("test")
        }
    }

    static final String robobeeSocket = '/tmp/robobee@robobee-test:22'

    @Before
    void beforeMethod() {
        assumeTrue "$robobeeSocket available", new File(robobeeSocket).exists()
    }
}
