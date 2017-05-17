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
package com.anrisoftware.sscontrol.rkt.debian.internal.rkt_1_2x_debian_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.rkt.service.internal.RktImpl.RktImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractRktScriptTest extends AbstractScriptTestBase {

    @Inject
    SshImplFactory sshFactory

    @Inject
    RktImplFactory rktFactory

    @Inject
    Rkt_Debian_Factory scriptFactory

    String getServiceName() {
        'rkt-1.26'
    }

    String getScriptServiceName() {
        'rkt-1.26/debian/8'
    }

    void createDummyCommands(File dir) {
        createIdCommand dir
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'systemctl',
            'which',
            'mv',
            'basename',
            'wget',
            'useradd',
            'tar',
            'grep',
            'apt-get',
            'gpg',
            'dpkg',
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'rkt', rktFactory
        services.putAvailableScriptService 'rkt-1.26/debian/8', scriptFactory
    }

    List getAdditionalModules() {
        RktModules.getAdditionalModules()
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
