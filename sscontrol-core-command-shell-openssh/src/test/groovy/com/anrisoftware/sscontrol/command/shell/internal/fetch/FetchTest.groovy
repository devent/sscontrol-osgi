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
package com.anrisoftware.sscontrol.command.shell.internal.fetch

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.command.fetch.external.Fetch
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
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

    @BeforeEach
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
