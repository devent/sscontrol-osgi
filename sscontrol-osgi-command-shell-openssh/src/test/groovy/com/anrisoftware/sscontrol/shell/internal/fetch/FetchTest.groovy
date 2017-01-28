/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-osgi-command-shell-openssh.
 *
 * sscontrol-osgi-command-shell-openssh is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-osgi-command-shell-openssh is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-osgi-command-shell-openssh. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.shell.internal.fetch

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.fetch.external.Fetch
import com.anrisoftware.sscontrol.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.google.inject.Module

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FetchTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    FetchFactory fetchFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test
    void "src"() {
        def test = [
            name: "src",
            args: [
                src: "aaa.txt",
                dest: null,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource FetchTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FetchTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FetchTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource FetchTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "directory_src"() {
        def test = [
            name: "directory_src",
            args: [
                src: "/var/wordpress",
                dest: null,
                recursive: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource FetchTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FetchTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FetchTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource FetchTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "dest_src"() {
        def test = [
            name: "dest_src",
            args: [
                src: "aaa.txt",
                dest: "/tmp",
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource FetchTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FetchTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FetchTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource FetchTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "privileged_src"() {
        def test = [
            name: "privileged_src",
            args: [
                src: "aaa.txt",
                dest: "/tmp",
                privileged: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource FetchTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FetchTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FetchTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource FetchTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "privileged_dir_src"() {
        def test = [
            name: "privileged_dir_src",
            args: [
                src: "/var/wordpress",
                dest: null,
                recursive: true,
                privileged: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource FetchTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource FetchTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource FetchTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource FetchTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    def createCmd(Map test, File tmp, int k) {
        def fetch = fetchFactory.create test.args, test.host, this, threads, log
        createEchoCommands tmp, [
            'mkdir',
            'chown',
            'chmod',
            'cp',
            'rm',
            'sudo',
            'scp',
        ]
        return fetch
    }

    @Before
    void setupTest() {
        super.setupTest()
        this.threads = CmdUtilsModules.getThreads(injector)
    }

    Module[] getAdditionalModules() {
        [
            new CmdModule(),
            new SshShellModule(),
            new FetchModule(),
            new ScpModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
