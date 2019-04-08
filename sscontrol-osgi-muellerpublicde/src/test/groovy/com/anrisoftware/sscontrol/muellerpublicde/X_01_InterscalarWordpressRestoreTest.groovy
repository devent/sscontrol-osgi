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
 * @author Erwin Müller, erwin.mueller@deventm.demariadb-6866ccdd84-d7dd8
 * @since 1.0
 */
@Slf4j
class X_01_InterscalarWordpressRestoreTest extends Abstract_Runner_Debian_Test {

	@Test
	void "restore-interscalar-com"() {
		def test = [
			name: "restore-interscalar-com",
			script: '''
service "ssh" with {
    host "robobee@andrea-master-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "ssh", group: "backup" with {
    host "robobee@localhost", socket: localhostSocket
}
service "k8s-cluster" with {
}

def dailyPath = "/home/${System.getProperty('user.name')}/Private/andrea-master-0.muellerpublic.de/backups/daily"
service "restore", target: "backup" with {
    service namespace: "interscalar-com", name: "mariadb"
    origin dir: "${dailyPath}/interscalar-com/mariadb", arguments: "--delete"
    client key: k8sVars.rsync.key, proxy: true, timeout: "1h"
    source << [target: "/data", chown: "1001.root", chmod: "u=rwX,g=rX,o=rX"]
}
service "restore", target: "backup" with {
    service namespace: "interscalar-com", name: "wordpress"
    origin arguments: "-c --delete", dir: "${dailyPath}/interscalar-com/wordpress"
    client key: k8sVars.rsync.key, proxy: true, timeout: "1h"
    source << [target: "/data", chown: "33.33", chmod: "u=rwX,g=rX,o=rX"]
}
''',
			scriptVars: [
				localhostSocket: localRobobeeSocket,
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
		assumeLocalExists()
		assumeMastersExists()
	}
}
