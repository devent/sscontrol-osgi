package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractDockerRegistryScriptTest extends AbstractDockerRegistryRunnerTest {

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createEchoCommands dir, [
            'apt-get',
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'mv',
            'id',
            'which',
            'dpkg',
            'git',
        ]
    }
}
