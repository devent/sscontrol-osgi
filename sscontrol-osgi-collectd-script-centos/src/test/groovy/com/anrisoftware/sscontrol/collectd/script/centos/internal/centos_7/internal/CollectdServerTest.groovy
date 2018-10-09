package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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
class CollectdServerTest extends AbstractCollectdRunnerTest {

    @Test
    void "collectd_server"() {
	def test = [
	    name: "collectd_server",
	    script: '''
service "ssh", host: "robobee@192.168.56.230", socket: robobeeSocket
service "collectd", version: "5.7" with {
    config name: "99-write-graphite", script: """
LoadPlugin "write_graphite"
<Plugin "write_graphite">
<Node "graphite">
  Host "graphite"
  Port "2003"
  Prefix "collectd."
  #Postfix ""
  Protocol "tcp"
  LogSendErrors true
  EscapeCharacter "_"
  SeparateInstances true
  StoreRates true
  AlwaysAppendDS false
</Node>
</Plugin>
"""
    config name: "99-write-influxdb", script: """
LoadPlugin "network"
<Plugin "network">
  Server "myinfluxdb.com" "25826"
</Plugin>
"""
}
''',
	    scriptVars: [robobeeSocket: centosSocket],
	    expectedServicesSize: 2,
	    generatedDir: folder.newFolder(),
	    expected: { Map args ->
		assertStringResource CollectdServerTest, readRemoteFile('/etc/collectd.d/99-write-graphite.conf', "mail.robobee.test"), "${args.test.name}_write_graphite_conf_expected.txt"
		assertStringResource CollectdServerTest, readRemoteFile('/etc/collectd.d/99-write-influxdb.conf', "mail.robobee.test"), "${args.test.name}_write_influxdb_conf_expected.txt"
	    },
	]
	doTest test
    }

    @Before
    void beforeMethod() {
	assumeTrue "$centosSocket available", new File(centosSocket).exists()
    }

    @Override
    void createDummyCommands(File dir) {
    }

    @Override
    Map getScriptEnv(Map args) {
	getEmptyScriptEnv args
    }
}
