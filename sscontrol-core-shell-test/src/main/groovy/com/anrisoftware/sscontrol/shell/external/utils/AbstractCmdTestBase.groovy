/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.shell.external.utils

import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach

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

	@BeforeEach
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
