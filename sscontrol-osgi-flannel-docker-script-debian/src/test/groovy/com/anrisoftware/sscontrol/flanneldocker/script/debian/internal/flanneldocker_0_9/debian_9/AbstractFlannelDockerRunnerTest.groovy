package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach

import com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9.FlannelDockerDebianFactory
import com.anrisoftware.sscontrol.flanneldocker.service.internal.FlannelDockerImpl.FlannelDockerImplFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunnerModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.runner.test.external.AbstractRunnerTestBase
import com.anrisoftware.sscontrol.ssh.script.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.script.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class AbstractFlannelDockerRunnerTest extends AbstractRunnerTestBase {

    static final Map robobeetestEtcdCerts = [
        ca: AbstractFlannelDockerRunnerTest.class.getResource('robobee_test_etcd_ca.pem'),
        cert: AbstractFlannelDockerRunnerTest.class.getResource('robobee_test_etcd_kube_0_cert.pem'),
        key: AbstractFlannelDockerRunnerTest.class.getResource('robobee_test_etcd_kube_0_key.pem'),
    ]

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    FlannelDockerImplFactory serviceFactory

    @Inject
    FlannelDockerDebianFactory scriptFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'flannel-docker', serviceFactory
        services.putAvailableScriptService 'flannel-docker/debian/9', scriptFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll FlannelDockerModules.getAdditionalModules()
        modules
    }


    @BeforeEach
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
