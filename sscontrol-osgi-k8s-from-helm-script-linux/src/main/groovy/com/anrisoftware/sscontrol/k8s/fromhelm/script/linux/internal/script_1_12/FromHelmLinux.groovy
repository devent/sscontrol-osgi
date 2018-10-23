/*-
 * #%L
 * sscontrol-osgi - k8s-from-helm-script-linux
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_12

import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_12.KubectlClusterLinuxFactory
import com.anrisoftware.sscontrol.k8s.fromhelm.service.external.FromHelm

/**
 * From Helm service for Kubernetes 1.9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class FromHelmLinux extends AbstractFromHelmLinux {

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
		installHelm()
		initHelm()
		def file = createConfig()
		try {
			if (service.useRepo) {
				fromRepo(config: file)
			} else {
				fromChart(config: file)
			}
		} finally {
			deleteTmpFile file
		}
	}

	@Override
	ContextProperties getDefaultProperties() {
		linuxPropertiesProvider.get()
	}

	@Override
	def getLog() {
		log
	}
}
