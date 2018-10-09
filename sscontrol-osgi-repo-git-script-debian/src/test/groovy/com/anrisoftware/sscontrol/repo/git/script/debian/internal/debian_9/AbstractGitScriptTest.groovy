package com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractGitScriptTest extends AbstractGitRunnerTest {

    static final URL idRsa = AbstractGitScriptTest.class.getResource('id_rsa.txt')

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
            'dpkg',
            'which',
            'id',
            'git',
        ]
    }
}
