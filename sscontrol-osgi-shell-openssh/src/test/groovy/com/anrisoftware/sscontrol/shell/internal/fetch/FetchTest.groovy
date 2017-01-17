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

    static Map expectedResources = [
        src_sudo: FetchTest.class.getResource('src_sudo_expected.txt'),
        src_scp: FetchTest.class.getResource('src_scp_expected.txt'),
        src_cp: FetchTest.class.getResource('src_cp_expected.txt'),
        src_rm: FetchTest.class.getResource('src_rm_expected.txt'),
        directory_src_scp: FetchTest.class.getResource('directory_src_scp_expected.txt'),
        dest_src_scp: FetchTest.class.getResource('dest_src_scp_expected.txt'),
        dest_src_sudo: FetchTest.class.getResource('dest_src_sudo_expected.txt'),
        privileged_src_scp: FetchTest.class.getResource('privileged_src_scp_expected.txt'),
        privileged_src_sudo: FetchTest.class.getResource('privileged_src_sudo_expected.txt'),
        privileged_dir_src_scp: FetchTest.class.getResource('privileged_dir_src_scp_expected.txt'),
        privileged_dir_src_sudo: FetchTest.class.getResource('privileged_dir_src_sudo_expected.txt'),
        privileged_dir_src_cp: FetchTest.class.getResource('privileged_dir_src_cp_expected.txt'),
        privileged_dir_src_rm: FetchTest.class.getResource('privileged_dir_src_rm_expected.txt'),
    ]

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
                String name = args.name as String
                assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                assertStringContent fileToStringReplace(new File(dir, 'cp.out')), resourceToString(expectedResources["${name}_cp"] as URL)
                assertStringContent fileToStringReplace(new File(dir, 'rm.out')), resourceToString(expectedResources["${name}_rm"] as URL)
            },
        ]
        log.info '\n######### {}. {} #########\ncase: {}', test.name, test
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
                String name = args.name as String
                assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                assert new File(dir, 'sudo.out').isFile() == false
                assert new File(dir, 'cp.out').isFile() == false
                assert new File(dir, 'rm.out').isFile() == false
            },
        ]
        log.info '\n######### {}. {} #########\ncase: {}', test.name, test
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
                String name = args.name as String
                assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
            },
        ]
        log.info '\n######### {}. {} #########\ncase: {}', test.name, test
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
                String name = args.name as String
                assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
            },
        ]
        log.info '\n######### {}. {} #########\ncase: {}', test.name, test
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
                String name = args.name as String
                assertStringContent fileToStringReplace(new File(dir, 'scp.out')), resourceToString(expectedResources["${name}_scp"] as URL)
                assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                assertStringContent fileToStringReplace(new File(dir, 'cp.out')), resourceToString(expectedResources["${name}_cp"] as URL)
                assertStringContent fileToStringReplace(new File(dir, 'rm.out')), resourceToString(expectedResources["${name}_rm"] as URL)
            },
        ]
        log.info '\n######### {}. {} #########\ncase: {}', test.name, test
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
