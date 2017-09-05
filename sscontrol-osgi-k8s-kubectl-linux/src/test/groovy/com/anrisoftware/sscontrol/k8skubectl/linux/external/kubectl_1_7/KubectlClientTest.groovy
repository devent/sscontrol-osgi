package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.utils.fabric.test.external.AbstractFabricTest
import com.google.inject.Injector

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class KubectlClientTest extends AbstractFabricTest {

    Injector injector

    @Inject
    KubectlClientFactory kubectlFactory

    @Before
    void injectKubectl() {
        this.injector = KubectlClientTestModules.createInjector()
        this.injector.injectMembers(this)
    }

    @Test
    void "waitNodeReady"() {
        kubectlFactory.create()
    }
}
