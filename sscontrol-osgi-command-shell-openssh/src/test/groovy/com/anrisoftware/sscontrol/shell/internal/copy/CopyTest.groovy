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
package com.anrisoftware.sscontrol.shell.internal.copy

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

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

    @Test
    void "dest_src"() {
        def test = [
            name: 'dest_src',
            args: [
                src: "aaa.txt",
                dest: "/tmp",
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CopyTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CopyTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource CopyTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "recursive_dest_src"() {
        def test = [
            name: "recursive_dest_src",
            args: [
                src: "/home/devent",
                dest: "/tmp",
                recursive: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                String name = args.name as String
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CopyTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CopyTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource CopyTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "override_exists_dest_src"() {
        def test = [
            name: "override_exists_dest_src",
            args: [
                src: "/home/devent",
                dest: "/",
                override: false,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                String name = args.name as String
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assert new File(dir, 'scp.out').isFile() == false
                assert new File(dir, 'cp.out').isFile() == false
                assert new File(dir, 'rm.out').isFile() == false
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
                String name = args.name as String
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CopyTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CopyTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource CopyTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "privileged_recursive_src"() {
        def test = [
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
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CopyTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource CopyTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource CopyTest, dir, "rm.out", "${args.test.name}_rm_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "privileged_override_exists_src"() {
        def test = [
            name: "privileged_override_exists_src",
            args: [
                src: "aaa.txt",
                dest: "/",
                privileged: true,
                override: false,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "direct_dest_src"() {
        def test = [
            name: "direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assert new File(dir, 'scp.out').isFile() == false
                assertFileResource CopyTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
                assert new File(dir, 'mv.out').isFile() == true
                assertFileResource CopyTest, dir, "mv.out", "${args.test.name}_mv_expected.txt"
                assert new File(dir, 'sudo.out').isFile() == true
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "privileged_direct_dest_src"() {
        def test = [
            name: "privileged_direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
                privileged: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assert new File(dir, 'scp.out').isFile() == false
                assertFileResource CopyTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
                assert new File(dir, 'mv.out').isFile() == true
                assertFileResource CopyTest, dir, "mv.out", "${args.test.name}_mv_expected.txt"
                assert new File(dir, 'sudo.out').isFile() == true
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    @Test
    void "md5_hash_direct_dest_src"() {
        def test = [
            name: "md5_hash_direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
                hash: "md5:d1b0c3ffb4dfd8d0f55a2a3d2a317d31",
            ],
            expected: { Map args ->
                File dir = args.dir as File
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'md5sum',
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
        }
    }

    @Test
    void "privileged_md5_hash_direct_dest_src"() {
        def test = [
            name: "privileged_md5_hash_direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
                hash: "md5:d1b0c3ffb4dfd8d0f55a2a3d2a317d31",
                privileged: true
            ],
            expected: { Map args ->
                File dir = args.dir as File
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'md5sum',
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
        }
    }

    @Test
    void "sha_hash_direct_dest_src"() {
        def test = [
            name: "sha_hash_direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
                hash: "sha:3d47bc8c8a81efe0b9e47ab4250f1a20ef8c308c",
            ],
            expected: { Map args ->
                File dir = args.dir as File
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'sha1sum',
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
        }
    }

    @Test
    void "sha1_hash_direct_dest_src"() {
        def test = [
            name: "sha1_hash_direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
                hash: "sha1:3d47bc8c8a81efe0b9e47ab4250f1a20ef8c308c",
            ],
            expected: { Map args ->
                File dir = args.dir as File
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'sha1sum',
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
        }
    }

    @Test
    void "sha256_hash_direct_dest_src"() {
        def test = [
            name: "sha1_hash_direct_dest_src",
            args: [
                src: "http://server.com/aaa.txt",
                dest: "/tmp",
                direct: true,
                hash: "sha256:c71b73872886f8fefdb8c9012a205e57e20bb54858884e0e0571d8df5f18763e",
            ],
            expected: { Map args ->
                File dir = args.dir as File
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'sha256sum',
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
        }
    }

    @Test
    void "sig_direct_url_dest_src"() {
        def test = [
            name: "sig_direct_url_dest_src",
            args: [
                src: "https://github.com/coreos/etcd/releases/download/v3.1.0/etcd-v3.1.0-linux-amd64.tar.gz",
                dest: "/tmp",
                direct: true,
                sig: "https://github.com/coreos/etcd/releases/download/v3.1.0/etcd-v3.1.0-linux-amd64.tar.gz.asc",
                key: "F804F4137EF48FD3",
                server: "pgp.uni-mainz.de"
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CopyTest, dir, "gpg.out", "${args.test.name}_gpg_expected.txt"
                assertFileResource CopyTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'gpg', //
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
        }
    }

    @Test
    void "sig_privileged_direct_url_dest_src"() {
        def test = [
            name: "sig_privileged_direct_url_dest_src",
            args: [
                src: "https://github.com/coreos/etcd/releases/download/v3.1.0/etcd-v3.1.0-linux-amd64.tar.gz",
                dest: "/tmp",
                direct: true,
                sig: "https://github.com/coreos/etcd/releases/download/v3.1.0/etcd-v3.1.0-linux-amd64.tar.gz.asc",
                key: "F804F4137EF48FD3",
                server: "pgp.uni-mainz.de",
                privileged: true,
            ],
            expected: { Map args ->
                File dir = args.dir as File
                assertFileResource CopyTest, dir, "sudo.out", "${args.test.name}_sudo_expected.txt"
                assertFileResource CopyTest, dir, "gpg.out", "${args.test.name}_gpg_expected.txt"
                assertFileResource CopyTest, dir, "wget.out", "${args.test.name}_wget_expected.txt"
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        createEchoCommands tmp, [
            'gpg', //
        ]
        test.host = SshFactory.localhost(injector).hosts[0]
        try {
            doTest test, tmp
        } catch (InvalidExitCodeException) {
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
