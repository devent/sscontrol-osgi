package com.anrisoftware.sscontrol.command.shell.internal.facts

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.sscontrol.command.facts.external.Facts
import com.anrisoftware.sscontrol.command.facts.external.Facts.FactsFactory
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.st.StModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractCmdTestBase
import com.anrisoftware.sscontrol.shell.external.utils.CmdUtilsModules
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Module

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FactsTest extends AbstractCmdTestBase {

    static Threads threads

    @Inject
    FactsFactory copyFactory

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test
    void "cat_release"() {
        def test = [
            name: "cat_release",
            args: [:],
            expected: { Map args ->
                Facts facts = args.cmd
                assert facts.system.system == 'linux'
                assert facts.system.name == 'debian'
                assert facts.system.version == '8'

                File dir = args.dir as File
                String name = args.name as String
                def catOut = fileToStringReplace(new File(dir, 'cat.out'))
                catOut = catOut.replaceAll("/etc/.*release", '/etc/release')
                assertStringContent catOut, resourceToString(FactsTest.class.getResource('cat_release_cat_expected.txt'))
            },
        ]
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def tmp = folder.newFolder()
        test.host = SshFactory.localhost(injector).hosts[0]
        doTest test, tmp
    }

    def createCmd(Map test, File tmp, int k) {
        def fetch = copyFactory.create test.args, test.host, this, threads, log
        createDebianJessieCatCommand tmp, 'cat'
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
            new SystemNameMappingsModule(),
            new TemplateResModule(),
            new SshShellModule(),
            new ShellCmdModule(),
            new CmdModule(),
            new CmdImplModule(),
            new FactsModule(),
            new ScpModule(),
            new CmdUtilsModules(),
            new StModule(),
        ] as Module[]
    }
}
