/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.command.shell.internal.copy

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import javax.inject.Inject

import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.copy.DownloadCopyWorker.DownloadCopyWorkerFactory
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.google.inject.Module

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
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

    @BeforeEach
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
