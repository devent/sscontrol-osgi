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
package com.anrisoftware.sscontrol.groovy.script.external

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.junit.jupiter.api.Assumptions.*

import javax.inject.Inject
import javax.inject.Provider

import org.junit.Rule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.globalpom.exec.internal.command.DefaultCommandLineModule
import com.anrisoftware.globalpom.exec.internal.core.DefaultProcessModule
import com.anrisoftware.globalpom.exec.internal.logoutputs.LogOutputsModule
import com.anrisoftware.globalpom.exec.internal.pipeoutputs.PipeOutputsModule
import com.anrisoftware.globalpom.exec.internal.runcommands.RunCommandsModule
import com.anrisoftware.globalpom.exec.internal.script.ScriptCommandModule
import com.anrisoftware.globalpom.exec.internal.scriptprocess.ScriptProcessModule
import com.anrisoftware.globalpom.threads.external.core.Threads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreads
import com.anrisoftware.globalpom.threads.properties.external.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.internal.PropertiesThreadsModule
import com.anrisoftware.resources.st.internal.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.st.internal.worker.STWorkerModule
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.internal.templates.TemplatesResourcesModule
import com.anrisoftware.sscontrol.command.copy.external.Copy.CopyFactory
import com.anrisoftware.sscontrol.command.fetch.external.Fetch.FetchFactory
import com.anrisoftware.sscontrol.command.replace.external.Replace.ReplaceFactory
import com.anrisoftware.sscontrol.command.shell.external.Cmd
import com.anrisoftware.sscontrol.command.shell.external.Shell.ShellFactory
import com.anrisoftware.sscontrol.command.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.command.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.command.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.command.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdImpl
import com.anrisoftware.sscontrol.command.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.command.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.command.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.command.shell.internal.st.StModule
import com.anrisoftware.sscontrol.command.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.ssh.service.internal.Localhost
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.transform.CompileStatic

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@EnableRuleMigrationSupport
class AbstractScriptBaseTest {

    @CompileStatic
    ScriptBase createScript(Map test) {
        def tmp = folder.newFolder()
        test.tmp = tmp
        ScriptBase script = test.input as ScriptBase
        script.env['PATH'] = '.'
        script.sudoEnv['PATH'] = '.'
        script.pwd = tmp
        script.chdir = tmp
        script.sudoChdir = tmp
        script.target = localhost
        script.shell = shell
        script.fetch = fetch
        script.copy = copy
        script.replace = replace
        script.threads = threads
        return script
    }

    static Injector injector

    static Threads threads

    static PropertiesThreadsFactory threadsFactory

    static Provider<? extends Properties> threadsProperties

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Inject
    CmdRunCaller cmdRunCaller

    @Inject
    Localhost localhost

    @Inject
    ShellFactory shell

    @Inject
    FetchFactory fetch

    @Inject
    CopyFactory copy

    @Inject
    ReplaceFactory replace

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
                new ShellCmdModule(),
                new CmdModule(),
                new FetchModule(),
                new ScpModule(),
                new CopyModule(),
                new ReplaceModule(),
                new TokensTemplateModule(),
                new SshShellModule(),
                new RunCommandsModule(),
                new LogOutputsModule(),
                new PipeOutputsModule(),
                new DefaultProcessModule(),
                new DefaultCommandLineModule(),
                new ScriptCommandModule(),
                new ScriptProcessModule(),
                new StModule(),
                new STDefaultPropertiesModule(),
                new STWorkerModule(),
                new TemplatesDefaultMapsModule(),
                new TemplatesResourcesModule(),
                new PropertiesThreadsModule(),
                new DurationSimpleFormatModule(),
                new DurationFormatModule(),
                new TemplateResModule(),
                new AbstractModule() {
                    protected void configure() {
                        bind Cmd to CmdImpl
                    }
                })
        this.threadsProperties = injector.getInstance ThreadsTestPropertiesProvider
        this.threadsFactory = injector.getInstance PropertiesThreadsFactory
        this.threads = createThreads()
    }

    static Threads createThreads() {
        PropertiesThreads threads = threadsFactory.create()
        threads.setProperties threadsProperties.get()
        threads.setName("script")
        threads
    }
}
