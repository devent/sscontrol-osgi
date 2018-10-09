package com.anrisoftware.sscontrol.shell.linux.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractShellScriptTestBase extends AbstractShellRunnerTestBase {

    void createDummyCommands(File dir) {
        createIdCommand dir
        createCommand exit1Command, dir, 'grep'
        createEchoCommands dir, [
            'cat',
            'cmd',
        ]
    }
}
