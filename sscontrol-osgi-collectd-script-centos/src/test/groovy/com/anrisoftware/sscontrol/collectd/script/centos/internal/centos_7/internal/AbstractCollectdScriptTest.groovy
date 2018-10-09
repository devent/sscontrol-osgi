package com.anrisoftware.sscontrol.collectd.script.centos.internal.centos_7.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import com.anrisoftware.sscontrol.utils.centos.external.CentosTestUtils

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractCollectdScriptTest extends AbstractCollectdRunnerTest {

    @Override
    void createDummyCommands(File dir) {
        createCommand CentosTestUtils.yumCommand, dir, 'yum'
        createCommand CentosTestUtils.catCommand, dir, 'cat'
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
            'make',
            'semodule'
        ]
    }
}
