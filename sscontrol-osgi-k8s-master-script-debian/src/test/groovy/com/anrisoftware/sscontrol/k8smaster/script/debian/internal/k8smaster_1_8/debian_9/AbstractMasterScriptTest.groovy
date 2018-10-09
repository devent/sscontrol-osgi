package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractMasterScriptTest extends AbstractMasterRunnerTest {

    static final Map certs = [
        ca: AbstractMasterScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractMasterScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractMasterScriptTest.class.getResource('cert_key.txt'),
    ]

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        new File(dir, "/etc/apt/sources.list.d").mkdirs()
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
            'which',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'curl',
            'sleep',
            'kubectl',
            'ufw',
            'modprobe',
            'kubeadm',
            'sed'
        ]
    }
}
