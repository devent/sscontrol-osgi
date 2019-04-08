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
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class D_02_MuellerpublicdeWordpressTest extends Abstract_Runner_Debian_Test {

	@Test
	void "muellerpublic-de-wordpress"() {
		def test = [
			name: "muellerpublic-de-wordpress",
			script: '''
service "ssh" with {
    host "robobee@andrea-node-0.muellerpublic.de", socket: socketFiles.masters[0]
}
service "k8s-cluster" with {
}
service "repo-git", group: "muellerpublic-de-wordpress" with {
    remote url: "git@github.com:robobee-repos/muellerpublic-de-wordpress-deploy.git"
    credentials "ssh", key: robobeeKey
	checkout branch: "release/r1"
}
service "from-repository", repo: "muellerpublic-de-wordpress" with {
    vars << [
        volume: [class: "managed-nfs-storage", storage: "5Gi"]
    ]
    vars << [
        db: [
            image: [name: 'mariadb', version: '10.3.8-bionic'],
            limits: [cpu: '100m', memory: '200Mi'],
            requests: [cpu: '100m', memory: '200Mi'],
            affinity: [key: "muellerpublic.de/muellerpublic-de", name: "required", required: true],
            root: [user: 'root', password: 'phaim8ohC0OhGh3eebu4quae1yoqu1To'],
            wordpress: [database: 'wordpressdb', user: 'wordpressdb', password: 'aiz1saJai5aeng3ohvooghijoaf7jah8'],
			initialDelaySeconds: "30"
        ]
    ]
    vars << [
        wordpress: [
	        image: [name: 'erwin82/muellerpublic-de-wordpress', version: 'v1-r1-3'],
	        limits: [cpu: '500m', memory: '500Mi'],
	        requests: [cpu: '500m', memory: '500Mi'],
	        affinity: [key: "muellerpublic.de/muellerpublic-de", name: "required", required: true],
			revision: "r1",
	        php: [
	            memoryLimit: "200M",
	            maxExecutionTime: 300,
	            maxChildren: 4,
	            startServers: 2,
	            minSpareServers: 2,
	            maxSpareServers: 2,
				maxRequests: 500,
				slowlogTimeout: 0,
				catchWorkersOutput: 1,
	            opcacheEnable: 1,
	            opcacheEnableCLI: 1,
	            opcacheMemoryConsumption: "64"
	        ],
			nginx: [
				initialDelaySeconds: "60",
				httpHeaders: [[name: "Host", value: "www.muellerpublic.de"]],
			],
			hosts: [
				'www.muellerpublic.de', // main domain
				'muellerpublic.de',
				//'www.mueller-public.de',
				//'mueller-public.de',
			],
        ]
    ]
    vars << [
        nginx: [
            image: [name: 'robobeerun/nginx', version: 'v1.13.12-r1'],
            limits: [cpu: '100m', memory: '100Mi'],
            requests: [cpu: '100m', memory: '100Mi'],
            affinity: [key: "muellerpublic.de/muellerpublic-de", name: "required", required: true],
			revision: "r4",
            workerProcesses: 4,
            workerConnections: 4096,
			readTimeout: 300,
			clientMaxBodySize: '64m',
        ]
    ]
    vars << [
        rsync: [
            image: [name: 'robobeerun/rsync', version: 'v3.1.2-r2'],
            limits: [cpu: '50m', memory: '50Mi'],
            requests: [cpu: '50m', memory: '50Mi'],
            affinity: [key: "muellerpublic.de/muellerpublic-de", name: "required", required: true],
            ssh: [revision: "r1", publicKey: k8sVars.rsync.publicKey],
        ]
    ]
}
''',
			scriptVars: [socketFiles: socketFiles, k8sVars: k8s_vars, robobeeKey: robobeeKey],
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
