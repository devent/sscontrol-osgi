package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17_debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractDockerceScriptTest extends AbstractDockerceRunnerTest {

    static final URL certCaPem = AbstractDockerceScriptTest.class.getResource('cert_ca.txt')

    static final URL muellerpublicCertCaPem = AbstractDockerceScriptTest.class.getResource('muellerpublic_de_ca_cert.pem')

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createEchoCommands dir, [
            'cat',
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'systemctl',
            'which',
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg',
            'update-grub',
            'dpkg',
        ]
    }
}
