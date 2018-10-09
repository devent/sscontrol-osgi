package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

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
class ZimbraServerTest extends AbstractZimbraRunnerTest {

    @Test
    void "zimbra_server"() {
        def test = [
            name: "zimbra_server",
            script: """
service "ssh", host: "robobee@mail.robobee.test", socket: "$zimbraSocket"
service "zimbra", version: "8.7"
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                //assertStringResource ZimbraServerTest, checkRemoteFiles('/opt/zimbra/*'), "${args.test.name}_zimbra_dir_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void beforeMethod() {
        assumeTrue "$zimbraSocket available", new File(zimbraSocket).exists()
        //assumeTrue zimbraHostAvailable
    }

    @Override
    void createDummyCommands(File dir) {
    }

    @Override
    Map getScriptEnv(Map args) {
        getEmptyScriptEnv args
    }
}
