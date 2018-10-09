package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
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
