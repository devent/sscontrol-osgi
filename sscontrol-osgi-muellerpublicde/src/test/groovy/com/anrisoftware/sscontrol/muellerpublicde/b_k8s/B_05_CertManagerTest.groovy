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
package com.anrisoftware.sscontrol.muellerpublicde.b_k8s

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.muellerpublicde.Abstract_Runner_Debian_Test
import com.anrisoftware.sscontrol.muellerpublicde.MuellerpublicdeResources

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class B_05_CertManagerTest extends Abstract_Runner_Debian_Test {

	@Test
	void "cert-manager"() {
		def test = [
			name: "cert-manager",
			script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "cert-manager" with {
    remote url: "git@github.com:robobee-repos/cert-manager.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/v0.4.1-r1"
}
service "from-repository", repo: "cert-manager", dest: "/etc/kubernetes/addons/cert-manager" with {
    vars << [
        certManager: [
            image: [version: 'v0.4.1'],
            limits: [cpu: '0', memory: '50Mi'],
            requests: [cpu: '0', memory: '50Mi'],
            allowOnMaster: false,
            affinity: [key: "robobeerun.com/cert-manager", name: "required", required: true],
        ],
    ]
    vars << [
        acme: [
            email: 'admin@muellerpublic.de',
        ]
    ]
}
''',
			scriptVars: [
				socketFiles: socketFiles,
				robobeeKey: robobeeKey,
			],
			expectedServicesSize: 4,
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
