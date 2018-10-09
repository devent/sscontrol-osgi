package com.anrisoftware.sscontrol.shell.external.utils

import static org.junit.Assume.*

import org.junit.Before

import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class AbstractCmdTestBase {

	Injector injector

	@Before
	void checkProfile() {
		def localTests = System.getProperty('project.custom.local.tests.enabled')
		assumeTrue localTests == 'true'
	}

	void doTest(Map test, File tmp, int k=0) {
		test.args["pwd"] = tmp
		test.args["chdir"] = tmp
		test.args["env"] = [:]
		test.args["env"]["PATH"] = "./"
		test.args["sudoEnv"] = [:]
		test.args["sudoEnv"]["PATH"] = "./"
		test.args["sudoChdir"] = tmp
		test.args["remoteTmp"] = tmp
		def host = SshFactory.localhost(injector).hosts[0]
		def parent = this
		def cmd = createCmd test, tmp, k
		cmd()
		Closure expected = test.expected
		expected([test: test, cmd: cmd, dir: tmp])
	}

	abstract Module[] getAdditionalModules()

	abstract def createCmd(Map test, File tmp, int k)

	void setupTest() {
		this.injector = Guice.createInjector(additionalModules).createChildInjector(
				new PropertiesModule(),
				new PropertiesUtilsModule(),
				new AbstractModule() {
					@Override
					protected void configure() {
						bind(HostServicePropertiesService).to(HostServicePropertiesImplFactory)
					}
				}
				)
		this.injector.injectMembers(this)
	}
}
