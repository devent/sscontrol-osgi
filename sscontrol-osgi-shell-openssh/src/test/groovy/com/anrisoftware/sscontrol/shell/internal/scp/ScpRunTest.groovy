/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-shell-openssh.
 *
 * sscontrol-osgi-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.scp

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject
import javax.inject.Provider

import org.joda.time.Duration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.ThreadsTestPropertiesProvider
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpRun.ScpRunFactory
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.util.logging.Slf4j

/**
 * 
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class ScpRunTest {

    @Test
    void "remote_src"() {
        def test = [
            name: 'remote_src',
            args: [
                debugLevel: 2,
                src: '/src/file.txt',
                dest: '/home/local',
                remoteSrc: true,
                remoteDest: false,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'rm',
            ],
            expected: [
                scp: 'remote_src_scp_out_expected.txt',
                mkdir: 'remote_src_mkdir_out_expected.txt',
                chown: 'remote_src_chown_out_expected.txt',
                chmod: 'remote_src_chmod_out_expected.txt',
                rm: 'remote_src_rm_out_expected.txt',
            ],
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "privileged_remote_src"() {
        def test = [
            name: 'remote_src',
            args: [
                debugLevel: 2,
                src: '/src/file.txt',
                dest: '/home/local',
                privileged: true,
                remoteSrc: true,
                remoteDest: false,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'rm',
            ],
            expected: [
                scp: 'privileged_remote_src_scp_out_expected.txt',
                mkdir: 'privileged_remote_src_mkdir_out_expected.txt',
                chown: 'privileged_remote_src_chown_out_expected.txt',
                chmod: 'privileged_remote_src_chmod_out_expected.txt',
                rm: 'privileged_remote_src_rm_out_expected.txt',
            ],
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "dest_src"() {
        def test = [
            name: 'dest_src',
            args: [
                debugLevel: 2,
                src: '/home/local/file.txt',
                dest: '/src',
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'find',
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'rm',
            ],
            expected: [
                scp: 'dest_src_scp_out_expected.txt'
            ],
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "privileged_dest_src"() {
        def test = [
            name: 'privileged_dest_src',
            args: [
                debugLevel: 2,
                src: '/usr/aaa.txt',
                dest: '/home/devent',
                privileged: true,
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'id',
                'rm',
            ],
            expected: [
                scp: 'privileged_dest_src_scp_out_expected.txt'
            ],
        ]
        runTestCases test, scpRunFactory
    }

    void runTestCases(Map test, ScpRunFactory scpFactory) {
        log.info '{} case: {}', test.name, test
        def defargs = [:]
        defargs.log = log
        defargs.timeout = Duration.standardSeconds(30)
        defargs.env = [PATH: './']
        defargs.sudoEnv = [PATH: './']
        defargs.sshHost = 'localhost'
        defargs.sshControlMaster = 'auto'
        defargs.sshControlPersistDuration = Duration.standardSeconds(10)
        Map args = new HashMap(defargs)
        args.putAll test.args
        args.chdir = folder.newFolder test.name
        args.sudoChdir = args.chdir
        createEchoCommands args.chdir, ['sudo']
        createEchoCommands args.chdir, test.commands
        def scp = scpFactory.create args, this, threads
        scp()
        Map testExpected = test.expected
        test.commands.each {
            def uri = testExpected[it]
            if (uri) {
                def res = ScpRunTest.class.getResource(uri)
                assertStringContent fileToStringReplace(toFile(args, it)), resourceToString(res)
            }
        }
    }

    File toFile(Map args, String name) {
        def file = new File(args.chdir, "${name}.out")
        assert file != null
        return file
    }

    static Injector injector

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    ScpRunFactory scpRunFactory

    @Before
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        this.injector = Guice.createInjector(
                new CmdModule(),
                new SshShellModule(),
                new ScpModule(),
                new CmdUtilsModules())
        this.threadsProperties = injector.getInstance ThreadsTestPropertiesProvider
        this.threadsFactory = injector.getInstance PropertiesThreadsFactory
        this.threads = createThreads()
    }

    static Threads createThreads() {
        PropertiesThreads threads = threadsFactory.create();
        threads.setProperties threadsProperties.get()
        threads.setName("script");
        threads
    }
}
