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
package com.anrisoftware.sscontrol.command.shell.internal.template

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.command.shell.internal.template.TemplateTest
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
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

    @Before
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
