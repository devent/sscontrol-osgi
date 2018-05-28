/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.k8s.fromhelm.service.internal.FromHelmImpl.FromHelmImplFactory
import com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_8.K8sClusterLinuxFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9.GitRepoDebianFactory
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
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
abstract class AbstractFromHelmRunnerTest extends AbstractRunnerTestBase {

	static final URL helmCommand = AbstractFromHelmRunnerTest.class.getResource('helm_command.txt')

	@Inject
	RunScriptImplFactory runnerFactory

	@Inject
	SshImplFactory sshFactory

	@Inject
	Ssh_Linux_Factory sshLinuxFactory

	@Inject
	K8sClusterFactory clusterFactory

	@Inject
	K8sClusterLinuxFactory clusterLinuxFactory

	@Inject
	GitRepoImplFactory gitFactory

	@Inject
	GitRepoDebianFactory gitDebianFactory

	@Inject
	FromHelmImplFactory fromHelmFactory

	@Inject
	FromHelmLinuxFactory fromHelmLinuxFactory

	def getRunScriptFactory() {
		runnerFactory
	}

	HostServices putServices(HostServices services) {
		services.putAvailableService 'ssh', sshFactory
		services.putAvailableScriptService 'ssh/linux/0', sshLinuxFactory
		services.putAvailableService 'k8s-cluster', clusterFactory
		services.putAvailableScriptService 'k8s/cluster/linux/0', clusterLinuxFactory
		services.putAvailableService 'repo-git', gitFactory
		services.putAvailableScriptService 'repo-git/debian/9', gitDebianFactory
		services.putAvailableService 'from-helm', fromHelmFactory
		services.putAvailableScriptService 'from-helm/linux/0', fromHelmLinuxFactory
		return services
	}

	List getAdditionalModules() {
		def modules = super.additionalModules
		modules << new RunnerModule()
		modules << new Ssh_Linux_Module()
		modules.addAll FromRepositoryTestModules.getAdditionalModules()
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
