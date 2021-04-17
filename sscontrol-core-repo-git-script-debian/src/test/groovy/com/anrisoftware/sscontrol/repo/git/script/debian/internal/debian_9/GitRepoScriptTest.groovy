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
package com.anrisoftware.sscontrol.repo.git.script.debian.internal.debian_9

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport

import com.anrisoftware.sscontrol.shell.external.utils.LocalhostSocketCondition

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
@EnableRuleMigrationSupport
@EnabledIfSystemProperty(named = 'project.custom.local.tests.enabled', matches = 'true')
@ExtendWith(LocalhostSocketCondition.class)
class GitRepoScriptTest extends AbstractGitScriptTest {

    @Test
    void "script_git_ssh"() {
        def test = [
            name: "script_git_ssh",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "repo-git", group: "wordpress-app" with {
    remote url: "git://git@github.com/user/wordpress-app"
    credentials "ssh", key: idRsa
}
''',
            scriptVars: [localhostSocket: localhostSocket, idRsa: idRsa],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource GitRepoScriptTest, dir, "dpkg.out", "${args.test.name}_dpkg_expected.txt"
                assertFileResource GitRepoScriptTest, dir, "apt-get.out", "${args.test.name}_apt_get_expected.txt"
                assertFileResource GitRepoScriptTest, dir, "scp.out", "${args.test.name}_scp_expected.txt"
                assertFileResource GitRepoScriptTest, dir, "mkdir.out", "${args.test.name}_mkdir_expected.txt"
                assertFileResource GitRepoScriptTest, dir, "cp.out", "${args.test.name}_cp_expected.txt"
                assertFileResource GitRepoScriptTest, dir, "cat.out", "${args.test.name}_cat_expected.txt"
                assertFileResource GitRepoScriptTest, dir, "git.out", "${args.test.name}_git_expected.txt"
            },
        ]
        doTest test
    }

    @Test
    void "script_git_file"() {
        def test = [
            name: "script_git_file",
            script: '''
service "ssh", host: "localhost", socket: localhostSocket
service "repo-git", group: 'wordpress-app' with {
    remote url: "/user/wordpress-app.git"
}
''',
            scriptVars: [localhostSocket: localhostSocket, idRsa: idRsa],
            expectedServicesSize: 2,
            generatedDir: folder.newFolder(),
            expected: { Map args ->
                File dir = args.dir
                File gen = args.test.generatedDir
                assertFileResource GitRepoScriptTest, dir, "git.out", "${args.test.name}_git_expected.txt"
            },
        ]
        doTest test
    }

}
