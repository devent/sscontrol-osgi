package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_9.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFlannelDockerScriptTest extends AbstractFlannelDockerRunnerTest {

    static final Map testCerts = [
        ca: AbstractFlannelDockerScriptTest.class.getResource('cert_ca.txt'),
        cert: AbstractFlannelDockerScriptTest.class.getResource('cert_cert.txt'),
        key: AbstractFlannelDockerScriptTest.class.getResource('cert_key.txt'),
    ]

    static final Map andreaLocalEtcdCerts = [
        ca: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_ca_cert.pem'),
        cert: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_client_0_robobee_test_cert.pem'),
        key: AbstractFlannelDockerScriptTest.class.getResource('andrea_local_etcd_client_0_robobee_test_key_insecure.pem'),
    ]

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
        createCommand whichufwnotfoundCommand, dir, 'which'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service',
            'systemctl',
            'id',
            'sha256sum',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'gpg',
            'curl',
            'mktemp',
            'docker',
        ]
    }
}
