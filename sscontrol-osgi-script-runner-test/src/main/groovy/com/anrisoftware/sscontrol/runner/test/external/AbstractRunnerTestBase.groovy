/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.runner.test.external

import java.nio.charset.Charset

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.globalpom.core.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.parser.groovy.internal.ParserModule
import com.anrisoftware.sscontrol.parser.groovy.internal.ParserImpl.ParserImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.replace.ReplaceModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.shell.internal.templateres.TemplateResModule
import com.anrisoftware.sscontrol.types.external.host.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule

import groovy.util.logging.Slf4j

/**
 * Extend this class to test service scripts that are loaded from a resource.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class AbstractRunnerTestBase extends AbstractScriptTestBase {

    @Inject
    ParserImplFactory parserFactory

    @Inject
    HostServicesImplFactory servicesFactory

    void doTest(Map test, int k=0) {
        log.info '\n######### {}. case: {}', k, test
        File parent = folder.newFolder()
        File scriptFile = new File(parent, "Script.groovy")
        File dir = folder.newFolder()
        test.scriptVars = test.scriptVars == null ? [:] : test.scriptVars
        createDummyCommands dir
        if (test.script instanceof URL) {
            IOUtils.copy test.input.openStream(), new FileOutputStream(scriptFile)
        } else {
            FileUtils.write(scriptFile, test.script.toString(), Charset.defaultCharset())
        }
        def roots = [parent.toURI()] as URI[]
        def scriptEnv = getScriptEnv(dir: dir, test: test)
        def services = putServices(servicesFactory.create())
        def parser = parserFactory.create(roots, "Script.groovy", scriptEnv, services)
        def parsed = parser.parse()
        assert parsed.services.size() == test.expectedServicesSize
        runScriptFactory.create(threads, parsed).run scriptEnv
        Closure expected = test.expected
        expected test: test, services: services, dir: dir
    }

    abstract def getRunScriptFactory()

    String getServiceName() {
    }

    String getScriptServiceName() {
    }

    HostServices putServices(HostServices services) {
        services
    }

    List getAdditionalModules() {
        [
            new ParserModule(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new SshShellModule(),
            new ShellCmdModule(),
            new CmdImplModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new TemplateModule(),
            new FactsModule(),
            new TokensTemplateModule(),
            new TemplateResModule(),
        ]
    }
}
