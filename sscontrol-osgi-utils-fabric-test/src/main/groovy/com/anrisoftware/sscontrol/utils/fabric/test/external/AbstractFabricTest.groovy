package com.anrisoftware.sscontrol.utils.fabric.test.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.Rule

import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFabricTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(false)

    @Before
    void setupTest() {
        toStringStyle
    }

    def createMariadbDeploy() {
        server.expect().withPath("/apis/extensions/v1beta1/namespaces/test/deployments/mariadb").
                andReturn(200, new DeploymentBuilder().withNewMetadata()
                .withName("mariadb")
                .addToLabels("app", "mariadb")
                .withResourceVersion("1")
                .withGeneration(1L)
                .endMetadata()
                .withNewSpec()
                .withNewSelector()
                .addToMatchLabels("app", "mariadb")
                .endSelector()
                .withReplicas(1)
                .endSpec()
                .build()).once()
    }
}
