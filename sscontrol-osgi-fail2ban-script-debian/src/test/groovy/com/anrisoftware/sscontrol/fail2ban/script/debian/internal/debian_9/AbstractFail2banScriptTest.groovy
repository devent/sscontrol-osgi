package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static com.anrisoftware.sscontrol.utils.debian.external.Debian_9_TestUtils.*

import org.apache.commons.io.IOUtils

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFail2banScriptTest extends AbstractFail2banRunnerTest {

    static URL jail2banConf = AbstractFail2banScriptTest.class.getResource("fail2ban_conf.txt")

    static URL jailLocal = AbstractFail2banScriptTest.class.getResource("jail_local.txt")

    void createDummyCommands(File dir) {
        def configDir = new File(dir, 'etc/fail2ban')
        configDir.mkdirs()
        IOUtils.copy jail2banConf.openStream(), new FileOutputStream(new File(configDir, "fail2ban.conf"))
        IOUtils.copy jailLocal.openStream(), new FileOutputStream(new File(configDir, "jail.local"))
        createCommand catCommand, dir, "cat"
        createCommand grepCommand, dir, 'grep'
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
            'dpkg',
            'curl',
            'sleep',
            'docker',
            'kubectl',
            'ufw',
        ]
    }
}
