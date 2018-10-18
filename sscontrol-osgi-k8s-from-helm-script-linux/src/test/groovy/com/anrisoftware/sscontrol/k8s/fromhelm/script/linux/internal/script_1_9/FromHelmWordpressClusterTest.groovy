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
package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.sscontrol.types.host.external.HostServiceScript

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FromHelmWordpressClusterTest extends AbstractFromHelmRunnerTest {

    @Test
    void "cluster wordpress from chart"() {
	def test = [
	    name: "cluster_wordpress_chart",
	    script: '''
service "ssh", host: "node-0.robobee-test.test", socket: robobeeSocket
service "from-helm", chart: "stable/mariadb" with {
}
''',
	    scriptVars: [
		robobeeSocket: robobeeSocket,
		robobeeKey: robobeeKey,
		robobeePub: robobeePub,
	    ],
	    expectedServicesSize: 2,
	    expected: { Map args ->
	    },
	]
	doTest test
    }

    @BeforeEach
    void beforeMethod() {
	checkRobobeeSocket()
    }

    Map getScriptEnv(Map args) {
	getEmptyScriptEnv args
    }

    void createDummyCommands(File dir) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
	return script
    }
}
