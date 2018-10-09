package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_9.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractCollectdScriptTest extends AbstractCollectdRunnerTest {

    @Override
    void createDummyCommands(File dir) {
	createCommand catCommand, dir, 'cat'
	createCommand exit1Command, dir, 'dpkg'
	createCommand exit1Command, dir, 'grep'
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
	    'make',
	    'apt-get',
	]
    }
}
