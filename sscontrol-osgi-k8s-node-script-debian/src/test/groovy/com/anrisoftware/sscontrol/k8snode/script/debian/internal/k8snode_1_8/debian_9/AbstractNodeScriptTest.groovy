package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_8.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractNodeScriptTest extends AbstractNodeRunnerTest {

    static final Map certs = [
        ca: AbstractNodeScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractNodeScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractNodeScriptTest.class.getResource('cert_key.txt'),
    ]

    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        new File(dir, "/etc/apt/sources.list.d").mkdirs()
        createEchoCommands dir, [
            'id',
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'dpkg',
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
            'docker',
            'kubectl',
            'ufw',
            'modprobe',
            'kubeadm',
        ]
    }
}
