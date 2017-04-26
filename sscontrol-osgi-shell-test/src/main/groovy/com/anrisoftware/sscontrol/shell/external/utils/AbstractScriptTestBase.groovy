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
package com.anrisoftware.sscontrol.shell.external.utils

import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatModule
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
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.resources.st.internal.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.st.internal.worker.STWorkerModule
import com.anrisoftware.resources.templates.internal.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.internal.templates.TemplatesResourcesModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsModule
import com.anrisoftware.sscontrol.services.internal.TargetsServiceModule
import com.anrisoftware.sscontrol.services.internal.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.types.external.host.HostPropertiesService
import com.anrisoftware.sscontrol.types.external.host.HostService
import com.anrisoftware.sscontrol.types.external.host.HostServiceScript
import com.anrisoftware.sscontrol.types.external.host.HostServices
import com.anrisoftware.sscontrol.types.external.ssh.SshHost
import com.anrisoftware.sscontrol.types.external.ssh.TargetsService
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.util.logging.Slf4j

/**
 * Extend this class to test service scripts.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class AbstractScriptTestBase {

    static final String LOCAL_PROFILE = 'project.custom.local.tests.enabled'

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    Injector injector

    @Inject
    RobobeeScriptFactory robobeeScriptFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    ThreadsTestPropertiesProvider threadsProperties

    @Inject
    PropertiesThreadsFactory threadsFactory

    Threads threads

    static boolean isTestHostAvailable() {
        return isHostAvailable('robobee-test')
    }

    static boolean isHostAvailable(List hosts) {
        def notAvailable = hosts.find { !isHostAvailable(it) }
        notAvailable == null
    }

    /**
     * Checks if the specified host is available. The port 7 (Echo) must be
     * open on the host.
     */
    static boolean isHostAvailable(String host) {
        boolean a = false
        try {
            def address = InetAddress.getByName host
            a = address.isReachable(2000)
        } catch (UnknownHostException e) {
        }
        if (!a) {
            log.info 'Test host `{}` not available.', host
        }
        return a
    }

    /**
     * Checks if the profile with the specified name is activated.
     *
     * see #LOCAL_PROFILE
     */
    void checkProfile(String name) {
        def localTests = System.getProperty(name)
        assumeTrue localTests != true
    }

    void doTest(Map test, int k=0) {
        log.info '\n######### {}. case: {}', k, test
        test.scriptVars = test.scriptVars == null ? [:] : test.scriptVars
        File parent = folder.newFolder()
        File scriptFile = new File(parent, "Script.groovy")
        File dir = folder.newFolder()
        test.dir = dir
        test.pretest ? test.pretest(test) : false
        def services = servicesFactory.create()
        putServices services
        putSshService services
        services = robobeeScriptFactory.create scriptFile, test.input, test.scriptVars, services call()
        createDummyCommands dir
        def scriptEnv = getScriptEnv(dir: dir, test: test)
        services.getServices().each { String name ->
            List<HostService> service = services.getServices(name)
            service.eachWithIndex { HostService s, int i ->
                if (s.name == serviceName) {
                    setupHostService s, dir: dir
                    List<SshHost> targets = getTargets(services, s)
                    targets.each { SshHost host ->
                        log.info '{}. {} {} {}', i, name, s, host
                        HostServiceScript script = services.getAvailableScriptService(scriptServiceName).create(services, s, host, threads, scriptEnv)
                        setupServiceScript script, dir: dir
                        script.run()
                    }
                }
            }
        }
        Closure expected = test.expected
        expected test: test, services: services, dir: dir
    }

    abstract String getServiceName()

    abstract String getScriptServiceName()

    abstract void createDummyCommands(File dir)

    abstract HostServices putServices(HostServices services)

    abstract List getAdditionalModules()

    List getTargets(HostServices services, HostService service) {
        service.targets.size() == 0 ? services.targets.getHosts('default') : service.targets
    }

    void putSshService(HostServices services) {
        services.addService 'ssh', SshFactory.localhost(injector)
    }

    def setupHostService(Map args, HostService service) {
    }

    def setupServiceScript(Map args, HostServiceScript script) {
    }

    Map getScriptEnv(Map args) {
        def map = new HashMap(args.test.scriptVars)
        map.chdir = args.dir
        map.pwd = args.dir
        map.base = args.dir
        map.sudoEnv = [:]
        map.sudoEnv.PATH = args.dir
        map.env = [:]
        map.env.PATH = args.dir
        map.createTmpFileCallback = createGenerateTempDir(args)
        map.filesLocal = true
        return map
    }

    Map getEmptyScriptEnv(Map args) {
        def map = new HashMap(args.test.scriptVars)
        map.sudoEnv = [:]
        map.env = [:]
        return map
    }

    def createGenerateTempDir(Map args) {
        //.
        { Map a ->
            def file = null
            switch (a.cmdName) {
                case 'template':
                    def split = StringUtils.split(a.dest.toString(), File.separator)
                    def name = split[3..split.length-1].join(File.separator)
                    assert args.test.generatedDir != null : "generatedDir must be set in test case"
                    file = new File(args.test.generatedDir, name)
                    break
            }
            return file ? file : folder.newFile()
        }
    }

    Injector createInjector() {
        this.injector = Guice.createInjector(
                new RobobeeScriptModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesModule(),
                new PropertiesUtilsModule(),
                new RunCommandsModule(),
                new LogOutputsModule(),
                new PipeOutputsModule(),
                new DefaultProcessModule(),
                new DefaultCommandLineModule(),
                new ScriptCommandModule(),
                new ScriptProcessModule(),
                new STDefaultPropertiesModule(),
                new STWorkerModule(),
                new TemplatesDefaultMapsModule(),
                new TemplatesResourcesModule(),
                new PropertiesThreadsModule(),
                new DurationSimpleFormatModule(),
                new DurationFormatModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                })
        this.injector = injector.createChildInjector(additionalModules)
        injector
    }

    Threads createThreads() {
        PropertiesThreads threads = threadsFactory.create()
        threads.setProperties threadsProperties.get()
        threads.setName("script")
        threads
    }
}
