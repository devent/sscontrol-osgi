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
package com.anrisoftware.sscontrol.repo.git.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.repo.git.service.external.GitRepo
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.Ssh
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class GitRepoScriptTest {

    @Inject
    GitRepoImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "file"() {
        def test = [
            name: 'git_url',
            input: """
service "repo-git", group: 'wordpress-app' with {
    remote url: "/devent/wordpress-app"
}
""",
            expected: { HostServices services ->
                assert services.getServices('repo-git').size() == 1
                GitRepo s = services.getServices('repo-git')[0] as GitRepo
                assert s.name == 'repo-git'
                assert s.group == 'wordpress-app'
                assert s.remote.uri.toString() == 'file:/devent/wordpress-app'
            },
        ]
        doTest test
    }

    @Test
    void "git_url"() {
        def test = [
            name: 'git_url',
            input: """
service "repo-git", group: 'wordpress-app' with {
    remote url: "git://github.com/devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa.pub"
}
""",
            expected: { HostServices services ->
                assert services.getServices('repo-git').size() == 1
                GitRepo s = services.getServices('repo-git')[0] as GitRepo
                assert s.name == 'repo-git'
                assert s.group == 'wordpress-app'
                assert s.remote.uri.toString() == 'git://github.com/devent/wordpress-app.git'
                assert s.credentials.type == 'ssh'
                assert s.credentials.key.toString() == 'file:id_rsa.pub'
            },
        ]
        doTest test
    }

    @Test
    void "git_scp"() {
        def test = [
            name: 'git_scp',
            input: """
service "repo-git", group: 'wordpress-app' with {
    remote url: "git@github.com:devent/wordpress-app-test.git"
}
""",
            expected: { HostServices services ->
                assert services.getServices('repo-git').size() == 1
                GitRepo s = services.getServices('repo-git')[0] as GitRepo
                assert s.name == 'repo-git'
                assert s.group == 'wordpress-app'
                assert s.remote.uri.toString() == 'ssh://git@github.com/devent/wordpress-app-test.git'
            },
        ]
        doTest test
    }

    @Test
    void "git_scp_tag"() {
        def test = [
            name: 'git_scp_tag',
            input: """
service "repo-git", group: 'wordpress-app' with {
    remote url: "git@github.com:devent/wordpress-app-test.git"
    checkout branch: "master", tag: "yaml", commit: "e9edddc2e2a59ecb5526febf5044828e7fedd914"
}
""",
            expected: { HostServices services ->
                assert services.getServices('repo-git').size() == 1
                GitRepo s = services.getServices('repo-git')[0] as GitRepo
                assert s.name == 'repo-git'
                assert s.group == 'wordpress-app'
                assert s.remote.uri.toString() == 'ssh://git@github.com/devent/wordpress-app-test.git'
                assert s.checkout.branch == 'master'
                assert s.checkout.tag == 'yaml'
                assert s.checkout.commit == 'e9edddc2e2a59ecb5526febf5044828e7fedd914'
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget([getGroup: {'default'}, getHosts: { []}] as Ssh)
        services.putAvailableService 'repo-git', serviceFactory
        Eval.me 'service', services, test.input as String
        Closure expected = test.expected
        expected services
    }

    @BeforeEach
    void setupTest() {
        toStringStyle
        Guice.createInjector(
                new GitRepoModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new TargetsServiceModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                ).injectMembers(this)
    }
}
