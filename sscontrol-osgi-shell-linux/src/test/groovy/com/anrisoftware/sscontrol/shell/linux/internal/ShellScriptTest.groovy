package com.anrisoftware.sscontrol.shell.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.Test

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ShellScriptTest extends AbstractShellScriptTestBase {

    @Test
    void "shell_scripts"() {
        def test = [
            name: "shell_scripts",
            script: """
service "ssh", host: "localhost"
service "shell" with {
    script "cmd Hello"
    script << "cmd Hello"
}
""",
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource ShellScriptTest, dir, "cmd.out", "${args.test.name}_cmd_expected.txt"
            },
        ]
        doTest test
    }
}
