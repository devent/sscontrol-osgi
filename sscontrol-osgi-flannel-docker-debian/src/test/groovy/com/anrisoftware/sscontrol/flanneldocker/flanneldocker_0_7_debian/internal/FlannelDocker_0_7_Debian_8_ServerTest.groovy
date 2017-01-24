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
package com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.globalpom.strings.StringsModule
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokensTemplateModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal.FlannelDocker_0_7_Debian_8.FlannelDocker_0_7_Debian_8_Factory
import com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerModule
import com.anrisoftware.sscontrol.flanneldocker.internal.FlannelDockerImpl.FlannelDockerImplFactory
import com.anrisoftware.sscontrol.replace.internal.ReplaceModule
import com.anrisoftware.sscontrol.services.internal.HostServicesModule
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.shell.internal.cmd.CmdModule
import com.anrisoftware.sscontrol.shell.internal.copy.CopyModule
import com.anrisoftware.sscontrol.shell.internal.facts.FactsModule
import com.anrisoftware.sscontrol.shell.internal.fetch.FetchModule
import com.anrisoftware.sscontrol.shell.internal.scp.ScpModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdImplModule
import com.anrisoftware.sscontrol.shell.internal.ssh.CmdRunCaller
import com.anrisoftware.sscontrol.shell.internal.ssh.ShellCmdModule
import com.anrisoftware.sscontrol.shell.internal.ssh.SshShellModule
import com.anrisoftware.sscontrol.shell.internal.template.TemplateModule
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.external.HostServices
import com.anrisoftware.sscontrol.types.internal.TypesModule
import com.google.inject.AbstractModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FlannelDocker_0_7_Debian_8_ServerTest extends AbstractScriptTestBase {

    @Inject
    FlannelDockerImplFactory serviceFactory

    @Inject
    FlannelDocker_0_7_Debian_8_Factory scriptFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    CmdRunCaller cmdRunCaller

    @Test
    void "basic"() {
        def test = [
            name: "basic",
            input: """
service "ssh", host: "robobee@andrea-master", key: "$robobeeKey"

service "flannel-docker" with {
    etcd "http://127.0.0.1:2379"
}
""",
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
            },
        ]
        doTest test
    }

    String getServiceName() {
        'flannel-docker'
    }

    String getScriptServiceName() {
        'flannel-docker/debian/8'
    }

    void createDummyCommands(File dir) {
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'flannel-docker', serviceFactory
        services.putAvailableScriptService 'flannel-docker/debian/8', scriptFactory
        services.putAvailableService 'ssh', sshFactory
    }

    Map getScriptEnv(Map args) {
        def map = [:]
        map.chdir = null
        map.pwd = null
        map.base = null
        map.sudoEnv = [:]
        map.env = [:]
        map.createTmpFileCallback = null
        return map
    }

    List getAdditionalModules() {
        [
            new SshModule(),
            new SshPreModule(),
            new FlannelDockerModule(),
            new FlannelDocker_0_7_Debian_8_Module(),
            new DebugLoggingModule(),
            new TypesModule(),
            new StringsModule(),
            new HostServicesModule(),
            new ShellCmdModule(),
            new SshShellModule(),
            new CmdImplModule(),
            new CmdModule(),
            new ScpModule(),
            new CopyModule(),
            new FetchModule(),
            new ReplaceModule(),
            new FactsModule(),
            new TemplateModule(),
            new TokensTemplateModule(),
            new ResourcesModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                }
            }
        ]
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
