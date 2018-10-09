package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.centos.external.CentosTestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractZimbraScriptTest extends AbstractZimbraRunnerTest {

    @Override
    void createDummyCommands(File dir) {
        createCommand catCommand, dir, 'cat'
        createCommand yumCommand, dir, 'yum'
        createCommand AbstractZimbraScriptTest.class.getResource('wc_custom_command.txt'), dir, 'wc'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'systemctl',
            'which',
            'id',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'grep',
            'gpg',
            'sha256sum',
            'expect',
            'firewall-cmd',
            'docker',
        ]
    }
}
