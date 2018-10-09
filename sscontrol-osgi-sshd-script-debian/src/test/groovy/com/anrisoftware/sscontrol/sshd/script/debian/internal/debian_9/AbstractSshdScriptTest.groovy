package com.anrisoftware.sscontrol.sshd.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractSshdScriptTest extends AbstractSshdRunnerTest {

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createWhichCommand dir
        createIdCommand dir
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'systemctl',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'dpkg',
            'curl',
            'sleep',
            'docker',
            'cat',
            'kubectl',
        ]
    }
}
