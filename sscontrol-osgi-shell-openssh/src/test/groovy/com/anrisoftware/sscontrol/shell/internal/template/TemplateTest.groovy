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
package com.anrisoftware.sscontrol.shell.internal.template

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshModule
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

    static Map expectedResources = [
        base_dest_scp: TemplateTest.class.getResource('base_dest_scp_expected.txt'),
        resource_dest_scp: TemplateTest.class.getResource('resource_dest_scp_expected.txt'),
        privileged_base_dest_scp: TemplateTest.class.getResource('privileged_base_dest_scp_expected.txt'),
        privileged_base_dest_sudo: TemplateTest.class.getResource('privileged_base_dest_sudo_expected.txt'),
        privileged_base_dest_cp: TemplateTest.class.getResource('privileged_base_dest_cp_expected.txt'),
        privileged_base_dest_rm: TemplateTest.class.getResource('privileged_base_dest_rm_expected.txt'),
    ]

    @Test
    void "template cases"() {
        def testCases = [
            [
                enabled: true,
                name: "base_dest",
                args: [
                    base: "TemplateTestTemplates",
                    resource: "test_template",
                    name: "testTemplate",
                    dest: "${folder.newFile()}",
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                    assert new File(dir, 'sudo.out').isFile() == false
                    assert new File(dir, 'cp.out').isFile() == false
                    assert new File(dir, 'rm.out').isFile() == false
                },
            ],
            [
                enabled: true,
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
                    String name = args.name as String
                    assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                    assert new File(dir, 'sudo.out').isFile() == false
                    assert new File(dir, 'cp.out').isFile() == false
                    assert new File(dir, 'rm.out').isFile() == false
                },
            ],
            [
                enabled: true,
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
                    String name = args.name as String
                    assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                    assertStringContent fileToStringReplace(new File(dir, 'cp.out')), resourceToString(expectedResources["${name}_cp"] as URL)
                    assertStringContent fileToStringReplace(new File(dir, 'rm.out')), resourceToString(expectedResources["${name}_rm"] as URL)
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            if (test.enabled) {
                log.info '\n######### {}. case: {}', k, test
                def tmp = folder.newFolder()
                test.host = SshFactory.localhost(injector).hosts[0]
                doTest test, tmp, k
            }
        }
    }

    def createCmd(Map test, File tmp, int k) {
        def cmd = templateFactory.create test.args, test.host, this, threads, log
        cmd.tmpFile = folder.newFile()
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
            new SshModule(),
            new CmdModule(),
            new TemplateModule(),
            new ScpModule(),
            new CmdUtilsModules(),
        ] as Module[]
    }
}
