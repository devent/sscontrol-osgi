package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach

import com.anrisoftware.sscontrol.etcd.service.internal.EtcdImpl.EtcdImplFactory
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
abstract class AbstractEtcdRunnerTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = AbstractEtcdRunnerTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = AbstractEtcdRunnerTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = AbstractEtcdRunnerTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = AbstractEtcdRunnerTest.class.getResource('cert_key.txt')

    static final Map robobeetestEtcdCerts = [
        ca: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_ca.pem'),
        client: [
            ca: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_ca.pem'),
            cert: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_kube_0_cert.pem'),
            key: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_kube_0_key.pem'),
        ],
        etcd: [
            [
                cert: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_etcd_0_server_cert.pem'),
                key: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_etcd_0_server_key.pem'),
            ],
            [
                cert: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_etcd_1_server_cert.pem'),
                key: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_etcd_1_server_key.pem'),
            ],
            [
                cert: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_etcd_2_server_cert.pem'),
                key: AbstractEtcdRunnerTest.class.getResource('robobee_test_etcd_etcd_2_server_key.pem'),
            ],
        ],
    ]

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    EtcdImplFactory etcdFactory

    @Inject
    EtcdDebianFactory etcdDebianFactory

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
        services.putAvailableService 'etcd', etcdFactory
        services.putAvailableScriptService 'etcd/debian/9', etcdDebianFactory
        return services
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new Ssh_Linux_Module()
        modules.addAll EtcdModules.getAdditionalModules()
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
