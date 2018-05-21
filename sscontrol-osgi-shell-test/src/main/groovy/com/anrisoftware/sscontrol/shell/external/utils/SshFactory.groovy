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
package com.anrisoftware.sscontrol.shell.external.utils

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService
import com.anrisoftware.sscontrol.types.misc.external.DebugLogging
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.google.inject.Injector

import groovy.transform.ToString

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@ToString
class SshFactory implements Ssh {

	static Ssh localhost(Injector injector) {
		def ssh = injector.getInstance(SshFactory)
		ssh.hosts = [
			[
				getHost: { 'localhost' },
				getUser: { System.getProperty('user.name') },
				getPort: { 22 },
				getProto: { null },
				getKey: { },
				getHostAddress: { '127.0.0.1' },
				getSocket: {
				}

			] as SshHost
		]
		return ssh
	}

	static Ssh testServer(Injector injector) {
		def ssh = injector.getInstance(SshFactory)
		ssh.hosts = [
			[
				getHost: { 'robobee-test' },
				getUser: { 'robobee' },
				getPort: { 22 },
				getProto: { null },
				getKey: { UnixTestUtil.robobeeKey.toURI() },
				getHostAddress: { InetAddress.getByName('robobee').getHostAddress() },
				getSocket: {
				}
			] as SshHost
		]
		return ssh
	}

	List<SshHost> hosts = []

	List<SshHost> targets = []

	def serviceProperties

	@Inject
	SshFactory(HostServicePropertiesService propertiesService) {
		this.serviceProperties = propertiesService.create()
	}

	@Override
	String getName() {
		return "ssh"
	}

	@Override
	DebugLogging getDebugLogging() {
	}

	@Override
	SshHost getTarget() {
		targets[0]
	}

	@Override
	List<SshHost> getTargets() {
		targets
	}

	@Override
	String getGroup() {
		'default'
	}

	void setHosts(List<SshHost> hosts) {
		this.hosts = hosts
	}

	@Override
	List<SshHost> getHosts() {
		hosts
	}

	@Override
	HostServiceProperties getServiceProperties() {
		return serviceProperties
	}
}
