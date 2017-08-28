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
package com.anrisoftware.sscontrol.repo.git.linux.internal.debian_8

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.shell.external.utils.AbstractScriptTestBase
import com.anrisoftware.sscontrol.ssh.service.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.types.host.external.HostServices

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class Abstract_Git_Script_Debian_Test extends AbstractScriptTestBase {

    static final URL idRsa = Abstract_Git_Script_Debian_Test.class.getResource('id_rsa.txt')

    @Inject
    SshImplFactory sshFactory

    @Inject
    GitRepoImplFactory serviceFactory

    @Inject
    GitRepo_Debian_8_Factory scriptFactory

    String getServiceName() {
        'repo-git'
    }

    String getScriptServiceName() {
        'repo-git/debian/8'
    }

    void createDummyCommands(File dir) {
        createIdCommand dir
        createCommand exit1Command, dir, 'grep'
        createEchoCommands dir, [
            'apt-get',
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'mv',
            'cat',
            'git',
            'dpkg',
        ]
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailableService 'repo-git', serviceFactory
        services.putAvailableScriptService 'repo-git/debian/8', scriptFactory
    }

    List getAdditionalModules() {
        GitRepo_Test_Modules.getAdditionalModules()
    }

    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
