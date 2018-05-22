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
package com.anrisoftware.sscontrol.k8s.restore.service.internal.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.joda.time.Duration
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatModule
import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8s.restore.service.external.Restore
import com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreModule
import com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImpl.RestoreImplFactory
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScriptModule
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class RestoreServiceTest {

	@Inject
	RobobeeScriptFactory robobeeScriptFactory

	@Inject
	K8sClusterFactory clusterFactory

	@Inject
	RestoreImplFactory serviceFactory

	@Inject
	HostServicesImplFactory servicesFactory

	@Test
	void "source_dir_client"() {
		def test = [
			name: 'source_dir_client',
			script: '''
service "k8s-cluster"
service "restore" with {
    service namespace: "wordpress", name: "db"
    origin dir: "/mnt/backup", arguments: "--delete"
    client key: "id_rsa", config: """
ProxyCommand ssh -o ControlMaster=auto -o ControlPath=/tmp/robobee@%h:22 robobee@node nc %h %p
"""
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.service.namespace == 'wordpress'
				assert s.service.name == 'db'
				assert s.target.host == 'localhost'
				assert s.clusterHost.host == 'localhost'
				assert s.origin.dir.toString() == '/mnt/backup'
				assert s.origin.arguments == '--delete'
				assert s.client.config =~ 'ProxyCommand'
				assert s.client.key.toString() == 'file:id_rsa'
			},
		]
		doTest test
	}

	@Test
	void "service"() {
		def test = [
			name: 'service',
			script: '''
service "k8s-cluster"
service "restore" with {
    service namespace: "wordpress", name: "db"
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.service.namespace == 'wordpress'
				assert s.service.name == 'db'
			},
		]
		doTest test
	}

	@Test
	void "sources"() {
		def test = [
			name: 'sources',
			script: '''
service "k8s-cluster"
service "restore" with {
    source << [target: "/data", chown: "33.33"]
    source << [target: "/html", chown: "33.2014", chmod: "0660"]
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.sources.size() == 2
				assert s.sources[0].target == '/data'
				assert s.sources[0].chown == '33.33'
				assert s.sources[1].target == '/html'
				assert s.sources[1].chown == '33.2014'
				assert s.sources[1].chmod == '0660'
			},
		]
		doTest test
	}

	@Test
	void "source_dir_proxy"() {
		def test = [
			name: 'source_dir_proxy',
			script: '''
service "k8s-cluster"
service "restore" with {
    service namespace: "wordpress", name: "db"
    client key: "id_rsa", proxy: true
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.service.namespace == 'wordpress'
				assert s.service.name == 'db'
				assert s.target.host == 'localhost'
				assert s.clusterHost.host == 'localhost'
				assert s.client.key.toString() == 'file:id_rsa'
				assert s.client.proxy == true
			},
		]
		doTest test
	}

	@Test
	void "timeout_duration"() {
		def test = [
			name: 'timeout_duration',
			script: '''
service "k8s-cluster"
service "restore" with {
    client timeout: "PT1H"
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.target.host == 'localhost'
				assert s.clusterHost.host == 'localhost'
				assert s.client.timeout == Duration.standardMinutes(60)
			},
		]
		doTest test
	}

	@Test
	void "timeout_simple"() {
		def test = [
			name: 'timeout_simple',
			script: '''
service "k8s-cluster"
service "restore" with {
    client timeout: "1h"
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.target.host == 'localhost'
				assert s.clusterHost.host == 'localhost'
				assert s.client.timeout == Duration.standardMinutes(60)
			},
		]
		doTest test
	}

	@Test
	void "dry run"() {
		def test = [
			name: 'dryrun',
			script: '''
service "k8s-cluster"
service "restore", dryrun: true with {
}
''',
			scriptVars: [:],
			expected: { HostServices services ->
				assert services.getServices('restore').size() == 1
				Restore s = services.getServices('restore')[0]
				assert s.target.host == 'localhost'
				assert s.clusterHost.host == 'localhost'
				assert s.dryrun == true
			},
		]
		doTest test
	}

	void doTest(Map test) {
		log.info '\n######### {} #########\ncase: {}', test.name, test
		def services = servicesFactory.create()
		services.targets.addTarget SshFactory.localhost(injector)
		services.putAvailableService 'k8s-cluster', clusterFactory
		services.putAvailableService 'restore', serviceFactory
		robobeeScriptFactory.create folder.newFile(), test.script, test.scriptVars, services call()
		Closure expected = test.expected
		expected services
	}

	@Rule
	public TemporaryFolder folder = new TemporaryFolder()

	def injector

	@Before
	void setupTest() {
		toStringStyle
		injector = Guice.createInjector(
				new K8sModule(),
				new K8sClusterModule(),
				new RestoreModule(),
				new PropertiesModule(),
				new DebugLoggingModule(),
				new TypesModule(),
				new StringsModule(),
				new HostServicesModule(),
				new TargetsModule(),
				new TargetsServiceModule(),
				new PropertiesUtilsModule(),
				new ResourcesModule(),
				new TlsModule(),
				new RobobeeScriptModule(),
				new SystemNameMappingsModule(),
				new DurationFormatModule(),
				new DurationSimpleFormatModule(),
				new HostServicePropertiesServiceModule(),
				)
		injector.injectMembers(this)
	}
}
