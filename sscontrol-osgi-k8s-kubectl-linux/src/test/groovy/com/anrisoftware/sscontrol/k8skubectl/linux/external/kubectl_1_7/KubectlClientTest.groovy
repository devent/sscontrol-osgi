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
class KubectlClientTest extends AbstractFabricTest {

    @Inject
    KubectlClientFactory kubectlFactory

    @Override
    List getAdditionalModules() {
        KubectlClientTestModules.modules
    }

    @Test
    void "waitNodeReady"() {
        createTestNode()
        def script = injector.getInstance ScriptMock
        ClusterHost cluster = createClusterHost()
        def kubectl = kubectlFactory.create script, cluster
        kubectl.waitNodeReady 'node0', 10, TimeUnit.SECONDS
    }
}
