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
package com.anrisoftware.sscontrol.shell.internal.copy

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.DownloadCopyWorker.DownloadCopyWorkerFactory
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
class DownloadCopyWorkerTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    DownloadCopyWorkerFactory downloadFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test
    void "download_curl"() {
        def test = [
            name: "download_curl",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
            ],
            preTest: { Map args ->
                createEchoCommand args.dir, 'curl'
            },
            expected: { Map args ->
                File dir = args.dir as File
                String name = args.name as String
                assertFileResource CopyTest, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                assertFileResource CopyTest, dir, "mv.out", "${args.test.name}_mv_expected.txt"
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        test.preTest(dir: tmp)
        doTest test, tmp
    }

    @Test
    void "download_wget"() {
        def test = [
            name: "download_wget",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
            ],
            preTest: { Map args ->
                createEchoCommand args.dir, 'wget'
            },
            expected: { Map args ->
                File dir = args.dir as File
                String name = args.name as String
                assertFileResource CopyTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
                assertFileResource CopyTest, dir, "mv.out", "${args.test.name}_mv_expected.txt"
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        test.preTest(dir: tmp)
        doTest test, tmp
    }

    @Test
    void "download_privileged_curl"() {
        def test = [
            name: "download_privileged_curl",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                privileged: true,
            ],
            preTest: { Map args ->
                createEchoCommand args.dir, 'curl'
            },
            expected: { Map args ->
                File dir = args.dir as File
                String name = args.name as String
                assertFileResource CopyTest, dir, "curl.out", "${args.test.name}_curl_expected.txt"
                assertFileResource CopyTest, dir, "mv.out", "${args.test.name}_mv_expected.txt"
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        test.preTest(dir: tmp)
        doTest test, tmp
    }

    @Test
    void "download_privileged_wget"() {
        def test = [
            name: "download_privileged_wget",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                privileged: true,
            ],
            preTest: { Map args ->
                createEchoCommand args.dir, 'wget'
            },
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource CopyTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
                assertFileResource CopyTest, dir, "mv.out", "${args.test.name}_mv_expected.txt"
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        test.preTest(dir: tmp)
        doTest test, tmp
    }

    def createCmd(Map test, File tmp, int k) {
        def fetch = downloadFactory.create test.args, test.host, this, threads, log
        createWhichCommand tmp
        createBasenameCommand tmp
        createEchoCommands tmp, [
            'id',
            'mkdir',
            'chown',
            'chmod',
            'basename',
            'mv',
            'sudo',
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
            new SshShellModule(),
            new CmdModule(),
            new CopyModule(),
            new ScpModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
