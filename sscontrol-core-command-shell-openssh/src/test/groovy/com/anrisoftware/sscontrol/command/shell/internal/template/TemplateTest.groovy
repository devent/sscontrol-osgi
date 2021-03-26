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
package com.anrisoftware.sscontrol.command.shell.internal.template

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.template.external.Template.TemplateFactory
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
class TemplateTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    TemplateFactory templateFactory

    @Inject
    TemplatesFactory templatesFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test
    void "base_dest"() {
        def test = [
            name: "base_dest",
            args: [
                base: "TemplateTestTemplates",
                resource: "test_template",
                name: "testTemplate",
                dest: "${folder.newFile()}",
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource TemplateTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource TemplateTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource TemplateTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource TemplateTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "resource_dest"() {
        def test = [
            name: "resource_dest",
            args: [
                resource: {
                    def ts = templatesFactory.create('TemplateTestTemplates')
                    return ts.getResource('test_template')
                }(),
                name: "testTemplate",
                dest: "${folder.newFile()}",
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource TemplateTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource TemplateTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource TemplateTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource TemplateTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "privileged_base_dest"() {
        def test = [
            name: "privileged_base_dest",
            args: [
                base: "TemplateTestTemplates",
                resource: "test_template",
                name: "testTemplate",
                dest: "${folder.newFile()}",
                privileged: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource TemplateTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource TemplateTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource TemplateTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource TemplateTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    def createCmd(Map test, File tmp, int k) {
        def cmd = templateFactory.create test.args, test.host, this, threads, log
        cmd.tmpFile = folder.newFile()
        createIdCommand tmp
        createEchoCommands tmp, [
            'mkdir',
            'chown',
            'chmod',
            'cp',
            'rm',
            'sudo',
            'scp',
        ]
        return cmd
    }

    @BeforeEach
    void setupTest() {
        super.setupTest()
        this.threads = CmdUtilsModules.getThreads(injector)
    }

    Module[] getAdditionalModules() {
        [
            new TemplateResModule(),
            new SshShellModule(),
            new CmdModule(),
            new TemplateModule(),
            new ScpModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
