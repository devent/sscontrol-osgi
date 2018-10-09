package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractEtcdScriptTest extends AbstractEtcdRunnerTest {

    static final Map testCerts = [
        ca: AbstractEtcdScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractEtcdScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractEtcdScriptTest.class.getResource('cert_key.txt'),
    ]

    static final URL grepActiveCommand = AbstractEtcdScriptTest.class.getResource('grep_active_command.txt')

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        def d = new File(dir, '/usr/local/share')
        d.mkdirs()
        createFile EtcdScriptTest.class.getResource("etcdctl_vars.txt"), d, 'etcdctl-vars'
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
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg',
            'dpkg',
            'ifdown',
            'ifup',
            'source',
            'etcdctl',
        ]
    }
}
