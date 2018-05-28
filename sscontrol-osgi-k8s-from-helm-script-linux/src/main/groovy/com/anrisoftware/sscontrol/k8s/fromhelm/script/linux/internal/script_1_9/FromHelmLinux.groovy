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

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.FromHelm

/**
 * From Helm service for Kubernetes 1.9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class FromHelmLinux extends ScriptBase {

	KubectlClusterLinux kubectlClusterLinux

	@Inject
	FromHelmLinuxProperties linuxPropertiesProvider

	@Inject
	void setKubectlClusterLinuxFactory(KubectlClusterLinuxFactory factory) {
		this.kubectlClusterLinux = factory.create(scriptsRepository, service, target, threads, scriptEnv)
	}

	@Override
	def run() {
		FromHelm service = service
		assertThat "clusters=0 for $service", service.clusterHosts.size(), greaterThan(0)
		File dir = getState "${service.repo.type}-${service.repo.repo.group}-dir"
		assertThat "checkout-dir=null for $service", dir, notNullValue()
		try {
			parseTemplateFiles dir
			buildDocker dir
			kubeFiles dir
		} finally {
			shell "rm -rf $dir" call()
		}
	}

	@Override
	ContextProperties getDefaultProperties() {
		linuxPropertiesProvider.get()
	}

	List getKubectlFilesPatterns() {
		getScriptListProperty 'kubectl_files_patterns'
	}

	List getDockerfileFilesPatterns () {
		getScriptListProperty 'dockerfiles_files_patterns'
	}

	@Override
	def getLog() {
		log
	}
}
