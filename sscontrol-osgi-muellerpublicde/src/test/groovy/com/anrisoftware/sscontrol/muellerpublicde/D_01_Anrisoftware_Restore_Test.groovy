/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.muellerpublicde

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class D_01_Anrisoftware_Restore_Test extends Abstract_Runner_Debian_Test {

	@Test
	void "restore postgres anrisoftware-com"() {
		assumeLocalExists()
		def test = [
			name: "restore-postgres-anrisoftware-com",
			script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "backup" with {
    host "robobee@localhost", socket: localhostSocket
}
service "k8s-cluster" with {
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "restore", target: "backup", dryrun: false with {
    service namespace: "anrisoftware-com", name: "postgres"
    origin arguments: "-c --delete", dir: "${dailyPath}/anrisoftware-com/postgres/data"
    client key: k8sVars.rsync.key, proxy: true, timeout: "2h"
    source << [target: "/data", chown: "999.999", chmod: "u=rwX,g=rX,o=rX"]
}
''',
			scriptVars: [
				localhostSocket: localhostSocket,
				socketFiles: socketFiles,
				k8sVars: k8s_vars
			],
			expectedServicesSize: 3,
			expected: { Map args ->
			},
		]
		doTest test
	}

	@Test
	void "restore redmine anrisoftware-com"() {
		assumeLocalExists()
		def test = [
			name: "restore-redmine-anrisoftware-com",
			script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "backup" with {
    host "robobee@localhost", socket: localhostSocket
}
service "k8s-cluster" with {
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "restore", target: "backup", dryrun: false with {
    service namespace: "anrisoftware-com", name: "redmine"
    origin arguments: "-c --delete", dir: "${dailyPath}/anrisoftware-com/redmine"
    client key: k8sVars.rsync.key, proxy: true, timeout: "2h"
    source << [target: "/data", chown: "999.999", chmod: "u=rwX,g=rX,o=rX"]
}
''',
			scriptVars: [
				localhostSocket: localhostSocket,
				socketFiles: socketFiles,
				k8sVars: k8s_vars
			],
			expectedServicesSize: 3,
			expected: { Map args ->
			},
		]
		doTest test
	}

	@Test
	void "restore gitea anrisoftware-com"() {
		assumeLocalExists()
		def test = [
			name: "restore-gitea-anrisoftware-com",
			script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "backup" with {
    host "robobee@localhost", socket: localhostSocket
}
service "k8s-cluster" with {
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "restore", target: "backup", dryrun: false with {
    service namespace: "anrisoftware-com", name: "gitea"
    origin arguments: "-c --delete", dir: "${dailyPath}/anrisoftware-com/gitea"
    client key: k8sVars.rsync.key, proxy: true, timeout: "2h"
    source << [target: "/data/git", chown: "1000.1000", chmod: "u=rwX,g=rX,o=rX"]
    source << [target: "/data/gitea", chown: "1000.1000", chmod: "u=rwX,g=rX,o=rX"]
    source << [target: "/data/ssh", chown: "0.0", chmod: "u=rwX,g=,o="]
}
''',
			scriptVars: [
				localhostSocket: localhostSocket,
				socketFiles: socketFiles,
				k8sVars: k8s_vars
			],
			expectedServicesSize: 3,
			expected: { Map args ->
			},
		]
		doTest test
	}

	@Before
	void checkProfile() {
		assumeMastersExists()
		assumeNodesExists()
	}
}
