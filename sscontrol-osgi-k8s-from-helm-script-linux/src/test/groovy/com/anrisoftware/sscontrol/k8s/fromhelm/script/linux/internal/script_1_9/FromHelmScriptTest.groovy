package com.anrisoftware.sscontrol.k8s.fromhelm.script.linux.internal.script_1_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FromHelmScriptTest extends AbstractFromHelmScriptTest {

    @Test
    void "script wordpress from chart"() {
	def test = [
	    name: "script_wordpress_chart",
	    script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "from-helm", chart: "stable/mariadb" with {
}
''',
	    scriptVars: [localhostSocket: localhostSocket],
	    expectedServicesSize: 2,
	    generatedDir: folder.newFolder(),
	    expected: { Map args ->
		File dir = args.dir
		File gen = args.test.generatedDir
		assertFileResource FromHelmScriptTest, dir, "helm.out", "${args.test.name}_helm_expected.txt"
	    },
	]
	doTest test
    }

    @Before
    void checkProfile() {
	checkProfile LOCAL_PROFILE
	checkLocalhostSocket()
    }
}
