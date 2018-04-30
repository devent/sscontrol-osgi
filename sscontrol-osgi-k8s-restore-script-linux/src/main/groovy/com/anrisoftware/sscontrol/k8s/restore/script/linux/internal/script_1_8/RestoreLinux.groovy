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
package com.anrisoftware.sscontrol.k8s.restore.script.linux.internal.script_1_8

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClient
import com.anrisoftware.sscontrol.k8s.backup.client.external.RsyncClientFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.Source
import com.anrisoftware.sscontrol.k8s.backup.client.internal.DeploymentImpl
import com.anrisoftware.sscontrol.k8s.restore.service.external.Restore
import com.anrisoftware.sscontrol.k8s.restore.service.internal.ServiceImpl.ServiceImplFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials

import groovy.util.logging.Slf4j

/**
 * Restore service for Kubernetes.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class RestoreLinux extends ScriptBase {

	@Inject
	RestoreLinuxProperties propertiesProvider

	@Inject
	K8sClusterFactory clusterFactory

	@Inject
	RestoreWorkerImplFactory restoreWorkerFactory

	@Inject
	DeploymentFactory deployFactory

	@Inject
	ServiceImplFactory serviceFactory;

	DeploymentImpl deploy

	DeploymentImpl rsync

	RsyncClient rsyncClient

	KubectlClusterLinux kubectl

	@Inject
	void setRsyncClientFactory(RsyncClientFactory factory) {
		Restore service = service
		this.rsyncClient = factory.create(this, service.service, service.cluster, service.client, service.origin)
	}

	@Inject
	void setKubectlClusterLinuxFactory(KubectlClusterLinuxFactory factory) {
		this.kubectl = factory.create(scriptsRepository, service, target, threads, scriptEnv)
	}

	@Override
	def run() {
		Restore service = service
		this.deploy = deployFactory.create(service.clusterHost, kubectl, service.service)
		this.rsync = deployFactory.create(service.clusterHost, kubectl, serviceFactory.create([name: "rsync-${service.service.name}", namespace: service.service.namespace]))
		assertThat "cluster hosts > 0 for $service", service.clusterHosts.size(), greaterThan(0)
		setupDefaults()
		setupHosts()
		def origins = service.sources
		restoreWorkerFactory.create(service, deploy).with {
			init()
			try {
				before()
				start { Map args ->
					origins.each { Source origin ->
						rsyncClient.start(backup: false, path: origin.target, dir: service.origin.dir, port: args.rsyncPort)
						rsync.with {
							if (origin.chown) {
								execCommand rsyncDeploy, "chown", "${origin.chown}", "-R", "${origin.target}"
							}
							if (origin.chmod) {
								execCommand rsyncDeploy, "chmod", "${origin.chmod}", "-R", "${origin.target}"
							}
						}
					}
				}
			} finally {
				try {
					after()
				} finally {
					finally1()
				}
			}
		}
	}

	def setupDefaults() {
		Restore service = service
		if (!service.client.timeout) {
			service.client.timeout = timeoutLong
		}
		if (service.sources.size() == 0) {
			service.source defaultServiceTarget
		}
	}

	/**
	 * Setups the hosts.
	 */
	def setupHosts() {
		service.clusterHosts.each { setupHost it }
	}

	/**
	 * Setups the hosts.
	 */
	def setupHost(ClusterHost host) {
		Credentials c = host.credentials
		if (!host.proto) {
			if (c.hasProperty('tls') && c.tls.ca) {
				host.proto = defaultServerProtoSecured
			} else {
				host.proto = defaultServerProtoUnsecured
			}
		}
		if (!host.port) {
			if (c.hasProperty('tls') && c.tls.ca) {
				host.port = defaultServerPortSecured
			} else {
				host.port = defaultServerPortUnsecured
			}
		}
		return host
	}

	@Override
	ContextProperties getDefaultProperties() {
		propertiesProvider.get()
	}

	int getDefaultServerPortUnsecured() {
		properties.getNumberProperty 'default_server_port_unsecured', defaultProperties
	}

	int getDefaultServerPortSecured() {
		properties.getNumberProperty 'default_server_port_secured', defaultProperties
	}

	String getDefaultServerProtoUnsecured() {
		properties.getProperty 'default_server_proto_unsecured', defaultProperties
	}

	String getDefaultServerProtoSecured() {
		properties.getProperty 'default_server_proto_secured', defaultProperties
	}

	String getDefaultServiceTarget() {
		properties.getProperty 'default_service_target', defaultProperties
	}

	@Override
	def getLog() {
		log
	}
}
