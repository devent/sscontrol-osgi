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

    static Map expectedResources = [
        download_curl_curl: DownloadCopyWorkerTest.class.getResource('download_curl_curl_expected.txt'),
        download_curl_mv: DownloadCopyWorkerTest.class.getResource('download_curl_mv_expected.txt'),
        download_curl_sudo: DownloadCopyWorkerTest.class.getResource('download_curl_sudo_expected.txt'),
        download_wget_wget: DownloadCopyWorkerTest.class.getResource('download_wget_wget_expected.txt'),
        download_wget_mv: DownloadCopyWorkerTest.class.getResource('download_wget_mv_expected.txt'),
        download_wget_sudo: DownloadCopyWorkerTest.class.getResource('download_wget_sudo_expected.txt'),
        download_privileged_curl_curl: DownloadCopyWorkerTest.class.getResource('download_privileged_curl_curl_expected.txt'),
        download_privileged_curl_mv: DownloadCopyWorkerTest.class.getResource('download_privileged_curl_mv_expected.txt'),
        download_privileged_curl_sudo: DownloadCopyWorkerTest.class.getResource('download_privileged_curl_sudo_expected.txt'),
        download_privileged_wget_wget: DownloadCopyWorkerTest.class.getResource('download_privileged_wget_wget_expected.txt'),
        download_privileged_wget_mv: DownloadCopyWorkerTest.class.getResource('download_privileged_wget_mv_expected.txt'),
        download_privileged_wget_sudo: DownloadCopyWorkerTest.class.getResource('download_privileged_wget_sudo_expected.txt'),
    ]

    @Test
    void "download cases"() {
        def testCases = [
            [
                enabled: true,
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
                    assert new File(dir, 'curl.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'curl.out')), resourceToString(expectedResources["${name}_curl"] as URL)
                    assert new File(dir, 'mv.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'mv.out')), resourceToString(expectedResources["${name}_mv"] as URL)
                    assert new File(dir, 'sudo.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                },
            ],
            [
                enabled: true,
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
                    assert new File(dir, 'curl.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'curl.out')), resourceToString(expectedResources["${name}_curl"] as URL)
                    assert new File(dir, 'mv.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'mv.out')), resourceToString(expectedResources["${name}_mv"] as URL)
                    assert new File(dir, 'sudo.out').isFile() == true
                    assertStringContent fileToStringReplace(new File(dir, 'sudo.out')), resourceToString(expectedResources["${name}_sudo"] as URL)
                },
            ],
            [
                enabled: true,
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
                    String name = args.name as String
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
                test.preTest(dir: tmp)
                doTest test, tmp, k
            }
        }
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
