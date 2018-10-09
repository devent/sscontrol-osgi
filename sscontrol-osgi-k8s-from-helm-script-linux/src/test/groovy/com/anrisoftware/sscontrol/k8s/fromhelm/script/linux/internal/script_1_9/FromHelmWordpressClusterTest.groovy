package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

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

    @Before
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
