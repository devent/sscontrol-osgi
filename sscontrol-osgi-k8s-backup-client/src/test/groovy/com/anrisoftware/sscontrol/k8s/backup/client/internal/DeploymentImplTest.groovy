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
