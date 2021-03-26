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
package com.anrisoftware.sscontrol.command.shell.internal.scp

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import javax.inject.Inject
import javax.inject.Provider

import org.joda.time.Duration
import org.junit.jupiter.api.BeforeAll
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpRun.ScpRunFactory
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.ThreadsTestPropertiesProvider
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.util.logging.Slf4j

/**
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
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
            expected: { Map args ->
                File dir = args.dir
                assertFileResource ScpRunTest, dir, "scp.out", "${args.test.name}_scp_out_expected.txt"
                assertFileResource ScpRunTest, dir, "mkdir.out", "${args.test.name}_mkdir_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chown.out", "${args.test.name}_chown_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chmod.out", "${args.test.name}_chmod_out_expected.txt"
                assertFileResource ScpRunTest, dir, "rm.out", "${args.test.name}_rm_out_expected.txt"
            },
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "privileged_remote_src"() {
        def test = [
            name: 'privileged_remote_src',
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
            expected: { Map args ->
                File dir = args.dir
                assertFileResource ScpRunTest, dir, "scp.out", "${args.test.name}_scp_out_expected.txt"
                assertFileResource ScpRunTest, dir, "mkdir.out", "${args.test.name}_mkdir_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chown.out", "${args.test.name}_chown_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chmod.out", "${args.test.name}_chmod_out_expected.txt"
                assertFileResource ScpRunTest, dir, "rm.out", "${args.test.name}_rm_out_expected.txt"
            },
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
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'cp',
                'rm',
            ],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource ScpRunTest, dir, "scp.out", "${args.test.name}_scp_out_expected.txt"
                assertFileResource ScpRunTest, dir, "mkdir.out", "${args.test.name}_mkdir_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chown.out", "${args.test.name}_chown_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chmod.out", "${args.test.name}_chmod_out_expected.txt"
                assertFileResource ScpRunTest, dir, "rm.out", "${args.test.name}_rm_out_expected.txt"
            },
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "privileged_dest_src"() {
        def test = [
            name: 'privileged_dest_src',
            args: [
                debugLevel: 2,
                src: '/home/local/file.txt',
                dest: '/src',
                privileged: true,
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'cp',
                'rm',
            ],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource ScpRunTest, dir, "scp.out", "${args.test.name}_scp_out_expected.txt"
                assertFileResource ScpRunTest, dir, "mkdir.out", "${args.test.name}_mkdir_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chown.out", "${args.test.name}_chown_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chmod.out", "${args.test.name}_chmod_out_expected.txt"
                assertFileResource ScpRunTest, dir, "rm.out", "${args.test.name}_rm_out_expected.txt"
            },
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "dest_override_src"() {
        def test = [
            name: 'dest_override_src',
            args: [
                debugLevel: 2,
                src: '/home/local/file.txt',
                dest: '/notexists',
                override: false,
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'cp',
                'rm',
            ],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource ScpRunTest, dir, "scp.out", "${args.test.name}_scp_out_expected.txt"
                assertFileResource ScpRunTest, dir, "mkdir.out", "${args.test.name}_mkdir_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chown.out", "${args.test.name}_chown_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chmod.out", "${args.test.name}_chmod_out_expected.txt"
                assertFileResource ScpRunTest, dir, "rm.out", "${args.test.name}_rm_out_expected.txt"
            },
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "dest_override_exists_src"() {
        def test = [
            name: 'dest_override_exists_src',
            args: [
                debugLevel: 2,
                src: '/home/local/file.txt',
                dest: '/',
                override: false,
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'cp',
                'rm',
            ],
            expected: { Map args ->
                File dir = args.dir
                assert new File(dir, "scp.out").isFile() == false
            },
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "privileged_dest_override_src"() {
        def test = [
            name: 'privileged_dest_override_src',
            args: [
                debugLevel: 2,
                src: '/home/local/file.txt',
                dest: '/notexists',
                override: false,
                privileged: true,
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'cp',
                'rm',
            ],
            expected: { Map args ->
                File dir = args.dir
                assertFileResource ScpRunTest, dir, "scp.out", "${args.test.name}_scp_out_expected.txt"
                assertFileResource ScpRunTest, dir, "mkdir.out", "${args.test.name}_mkdir_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chown.out", "${args.test.name}_chown_out_expected.txt"
                assertFileResource ScpRunTest, dir, "chmod.out", "${args.test.name}_chmod_out_expected.txt"
                assertFileResource ScpRunTest, dir, "rm.out", "${args.test.name}_rm_out_expected.txt"
            },
        ]
        runTestCases test, scpRunFactory
    }

    @Test
    void "privileged_dest_override_exists_src"() {
        def test = [
            name: 'privileged_dest_override_exists_src',
            args: [
                debugLevel: 2,
                src: '/home/local/file.txt',
                dest: '/',
                override: false,
                privileged: true,
                remoteSrc: false,
                remoteDest: true,
            ],
            commands: [
                'scp',
                'mkdir',
                'chown',
                'chmod',
                'cp',
                'rm',
            ],
            expected: { Map args ->
                File dir = args.dir
                assert new File(dir, "scp.out").isFile() == false
            },
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
        createIdCommand args.chdir
        createEchoCommands args.chdir, ['sudo']
        createEchoCommands args.chdir, test.commands
        def scp = scpFactory.create args, this, threads
        scp()
        Closure expected = test.expected
        expected test: test, dir: args.chdir
    }

    static Injector injector

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    ScpRunFactory scpRunFactory

    @BeforeEach
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeAll
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

    @BeforeEach
    void checkProfile() {
        def localTests = System.getProperty('project.custom.local.tests.enabled')
        assumeTrue localTests == 'true'
    }

    static Threads createThreads() {
        PropertiesThreads threads = threadsFactory.create()
        threads.setProperties threadsProperties.get()
        threads.setName("script")
        threads
    }
}
