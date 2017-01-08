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
import com.anrisoftware.sscontrol.copy.external.Copy.CopyFactory
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
class CopyTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    CopyFactory copyFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    static Map expectedResources = [
        dest_src_scp: CopyTest.class.getResource('dest_src_scp_expected.txt'),
        recursive_dest_src_scp: CopyTest.class.getResource('recursive_dest_src_scp_expected.txt'),
        privileged_src_scp: CopyTest.class.getResource('privileged_src_scp_expected.txt'),
        privileged_src_sudo: CopyTest.class.getResource('privileged_src_sudo_expected.txt'),
        privileged_src_cp: CopyTest.class.getResource('privileged_src_cp_expected.txt'),
        privileged_src_rm: CopyTest.class.getResource('privileged_src_rm_expected.txt'),
        privileged_recursive_src_scp: CopyTest.class.getResource('privileged_recursive_src_scp_expected.txt'),
        privileged_recursive_src_sudo: CopyTest.class.getResource('privileged_recursive_src_sudo_expected.txt'),
        privileged_recursive_src_cp: CopyTest.class.getResource('privileged_recursive_src_cp_expected.txt'),
        privileged_recursive_src_rm: CopyTest.class.getResource('privileged_recursive_src_rm_expected.txt'),
        privileged_override_exists_src_sudo: CopyTest.class.getResource('privileged_override_exists_src_sudo_expected.txt'),
        direct_dest_src_wget: DownloadCopyWorkerTest.class.getResource('direct_dest_src_wget_expected.txt'),
        direct_dest_src_mv: DownloadCopyWorkerTest.class.getResource('direct_dest_src_mv_expected.txt'),
        direct_dest_src_sudo: DownloadCopyWorkerTest.class.getResource('direct_dest_src_sudo_expected.txt'),
        privileged_direct_dest_src_wget: DownloadCopyWorkerTest.class.getResource('privileged_direct_dest_src_wget_expected.txt'),
        privileged_direct_dest_src_mv: DownloadCopyWorkerTest.class.getResource('privileged_direct_dest_src_mv_expected.txt'),
        privileged_direct_dest_src_sudo: DownloadCopyWorkerTest.class.getResource('privileged_direct_dest_src_sudo_expected.txt'),
    ]

    @Test
    void "copy cases"() {
        def testCases = [
            [
                enabled: true,
                name: "dest_src",
                args: [
                    src: "aaa.txt",
                    dest: "/tmp",
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
                name: "recursive_dest_src",
                args: [
                    src: "/home/devent",
                    dest: "/tmp",
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
            ],
            [
                enabled: true,
                name: "override_exists_dest_src",
                args: [
                    src: "/home/devent",
                    dest: "/tmp",
                    override: false,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assert new File(dir, 'scp.out').isFile() == false
                    assert new File(dir, 'sudo.out').isFile() == false
                    assert new File(dir, 'cp.out').isFile() == false
                    assert new File(dir, 'rm.out').isFile() == false
                },
            ],
            [
                enabled: true,
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
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                    assertStringContent fileToStringReplace(new File(dir, 'cp.out')), resourceToString(expectedResources["${name}_cp"] as URL)
                    assertStringContent fileToStringReplace(new File(dir, 'rm.out')), resourceToString(expectedResources["${name}_rm"] as URL)
                },
            ],
            [
                enabled: true,
                name: "privileged_recursive_src",
                args: [
                    src: "/home/devent",
                    dest: "/tmp",
                    privileged: true,
                    recursive: true,
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
            [
                enabled: true,
                name: "privileged_override_exists_src",
                args: [
                    src: "aaa.txt",
                    dest: "/tmp",
                    privileged: true,
                    override: false,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assert new File(dir, 'scp.out').isFile() == false
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                    assert new File(dir, 'cp.out').isFile() == true
                    assert new File(dir, 'rm.out').isFile() == true
                },
            ],
            [
                enabled: true,
                name: "direct_dest_src",
                args: [
                    src: "http://server.com/aaa.txt",
                    dest: "/tmp",
                    direct: true,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assert new File(dir, 'scp.out').isFile() == false
                    assert new File(dir, 'wget.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'wget.out')), resourceToString(expectedResources["${name}_wget"] as URL)
                    assert new File(dir, 'mv.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'mv.out')), resourceToString(expectedResources["${name}_mv"] as URL)
                    assert new File(dir, 'sudo.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                },
            ],
            [
                enabled: true,
                name: "privileged_direct_dest_src",
                args: [
                    src: "http://server.com/aaa.txt",
                    dest: "/tmp",
                    direct: true,
                    privileged: true,
                ],
                expected: { Map args ->
                    File dir = args.dir as File
                    String name = args.name as String
                    assert new File(dir, 'scp.out').isFile() == false
                    assert new File(dir, 'wget.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'wget.out')), resourceToString(expectedResources["${name}_wget"] as URL)
                    assert new File(dir, 'mv.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'mv.out')), resourceToString(expectedResources["${name}_mv"] as URL)
                    assert new File(dir, 'sudo.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                },
            ],
        ]
        testCases.eachWithIndex { Map test, int k ->
            if (test.enabled) {
                log.info '\n######### {}. {} #########\ncase: {}', k, test.name, test
                def tmp = folder.newFolder()
                test.host = SshFactory.localhost(injector).hosts[0]
                doTest test, tmp, k
            }
        }
    }

    def createCmd(Map test, File tmp, int k) {
        def fetch = copyFactory.create test.args, test.host, this, threads, log
        createEchoCommands tmp, [
            'which',
            'id',
            'basename',
            'mv',
            'mkdir',
            'chown',
            'chmod',
            'cp',
            'rm',
            'sudo',
            'scp',
            'wget',
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
