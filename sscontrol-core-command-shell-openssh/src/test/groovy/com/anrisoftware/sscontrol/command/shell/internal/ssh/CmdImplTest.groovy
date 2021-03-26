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
package com.anrisoftware.sscontrol.command.shell.internal.ssh

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.jupiter.api.Assumptions.*

import javax.inject.Inject

import org.joda.time.Duration
import org.junit.Rule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.command.shell.external.Cmd
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
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
class CmdImplTest {

    @Test
    void "test commands with control master"() {
        def defargs = [:]
        defargs.log = log
        defargs.timeout = Duration.standardSeconds(5)
        defargs.sshHost = 'localhost'
        defargs.env = [PATH: './']
        defargs.sudoEnv = [PATH: './']
        defargs.sshControlMaster = 'auto'
        defargs.sshControlPersistDuration = Duration.standardSeconds(10)
        def testCases = [
            [
                name: 'one command',
                args: [debugLevel: 2],
                command: 'chmod +w a.txt',
                commands: ['chmod'],
                expected: [chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'multiple commands',
                args: [debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'no control master',
                args: [sshControlMaster: 'no', sshControlPath: '', debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
        ]
        def cmd = cmdRunCaller
        (0..2).each { runTestCases testCases, defargs, cmd, it }
    }

    @Test
    void "test commands"() {
        def defargs = [:]
        defargs.log = log
        defargs.timeout = Duration.standardSeconds(8)
        defargs.sshHost = 'localhost'
        defargs.env = [PATH: './']
        defargs.sudoEnv = [PATH: './']
        def testCases = [
            [
                name: 'one command',
                args: [debugLevel: 2],
                command: 'chmod +w a.txt',
                commands: ['chmod'],
                expected: [chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'multiple commands',
                args: [debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
            [
                name: 'no control master',
                args: [sshControlMaster: 'no', sshControlPath: '', debugLevel: 2],
                command: '''
touch a.txt
chmod +w a.txt
''',
                commands: ['touch', 'chmod'],
                expected: [touch: 'touch_file_out_expected.txt', chmod: 'chmod_file_out_expected.txt'],
            ],
        ]
        def cmd = cmdRunCaller
        runTestCases testCases, defargs, cmd
    }

    void runTestCases(List testCases, Map defargs, Cmd cmd, int i=0) {
        testCases.eachWithIndex { Map test, int k ->
            log.info '{}. case: "{}": {}', k, test.name, test
            String command = test.command as String
            Map args = new HashMap(defargs)
            args.putAll test.args
            args.chdir = folder.newFolder String.format('%03d_%03d_%s', i, k, test.name)
            args.sudoChdir = args.chdir
            createEchoCommands args.chdir, ['sudo']
            createEchoCommands args.chdir, test.commands
            cmd args, this, threads, command
            Map testExpected = test.expected
            test.commands.each { String it ->
                assertStringContent fileToStringReplace(new File(args.chdir, "${it}.out")), resourceToString(CmdImplTest.class.getResource(testExpected[it] as String))
            }
        }
    }

    static Injector injector

    static Threads threads

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    CmdRunCaller cmdRunCaller

    @BeforeEach
    void setupTest() {
        injector.injectMembers(this)
    }

    @BeforeEach
    void checkProfile() {
        def localTests = System.getProperty('project.custom.local.tests.enabled')
        assumeTrue localTests == 'true'
    }

    @BeforeAll
    static void setupInjector() {
        toStringStyle
        this.injector = Guice.createInjector(
                new CmdModule(),
                new SshShellModule(),
                new CmdUtilsModules())
        this.threads = CmdUtilsModules.getThreads(injector)
    }
}
