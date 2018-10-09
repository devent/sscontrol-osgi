package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.Collectd_Centos_7_Factory
import com.anrisoftware.sscontrol.collectd.service.internal.CollectdImpl.CollectdImplFactory
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
abstract class AbstractCollectdRunnerTest extends AbstractRunnerTestBase {

    static final String centosSocket = '/tmp/robobee@mail.robobee.test:22'

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    CollectdImplFactory collectdFactory

    @Inject
    Collectd_Centos_7_Factory collectdCentosFactory

    def getRunScriptFactory() {
	runnerFactory
    }

    HostServices putServices(HostServices services) {
	services.putAvailableService 'ssh', sshFactory
	services.putAvailableScriptService 'ssh/linux/0', ssh_Linux_Factory
	services.putAvailableService 'collectd', collectdFactory
	services.putAvailableScriptService 'collectd-5.7/centos/7', collectdCentosFactory
	return services
    }

    List getAdditionalModules() {
	def modules = super.additionalModules
	modules << new RunnerModule()
	modules << new Ssh_Linux_Module()
	modules.addAll CollectdModules.getAdditionalModules()
	modules
    }

    @Before
    void setupTest() {
	toStringStyle
	injector = createInjector()
	injector.injectMembers(this)
	this.threads = createThreads()
    }
}
