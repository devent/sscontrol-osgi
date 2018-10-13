package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class CollectdScriptTest extends AbstractCollectdScriptTest {

    @Test
    void "collectd_script"() {
        def test = [
            name: "collectd_script",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
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
}
''',
            scriptVars: [localhostSocket: localhostSocket],
            generatedDir: folder.newFolder(),
            expectedServicesSize: 2,
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource CollectdScriptTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CollectdScriptTest, dir, "yum.out", "${args.test.name}_yum_expected.txt"
                assertFileResource CollectdScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CollectdScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource CollectdScriptTest, dir, "systemctl.out", "${args.test.name}_systemctl_expected.txt"
            },
        ]
        doTest test
    }

    @BeforeEach
    void checkProfile() {
        checkProfile LOCAL_PROFILE
        checkLocalhostSocket()
    }
}
