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
package com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8s.fromrepository.service.external.FromRepository
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryModule
import com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImpl.FromRepositoryImplFactory
import com.anrisoftware.sscontrol.k8sbase.base.service.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesServiceModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoModule
import com.anrisoftware.sscontrol.repo.git.service.internal.GitRepoImpl.GitRepoImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScriptModule
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j
import net.lingala.zip4j.core.ZipFile

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class FromRepositoryScriptTest {

    @Inject
    RobobeeScriptFactory robobeeScriptFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    FromRepositoryImplFactory fromRepositoryFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Inject
    GitRepoImplFactory gitFactory

    File tmpRepo

    @Test
    void "use external cluster in the group default implicit"() {
        def test = [
            name: 'cluster_default',
            script: '''
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app"
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.repo.repo.remote.uri.toString() == 'ssh://git@github.com/devent/wordpress-app.git'
                assert s.repo.repo.credentials.type == 'ssh'
                assert s.clusterHosts.size() == 1
                assert s.clusterHosts[0].target.host == "localhost"
            },
        ]
        doTest test
    }

    @Test
    void "use kubectl host on explicit host default"() {
        def test = [
            name: 'kubectl_host_default',
            script: '''
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app", cluster: "default"
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.repo.repo.remote.uri.toString() == 'ssh://git@github.com/devent/wordpress-app.git'
                assert s.repo.repo.credentials.type == 'ssh'
            },
        ]
        doTest test
    }

    @Test
    void "vars"() {
        def test = [
            name: 'vars',
            script: '''
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app" with {
    vars << [mysql: [version: "5.6", image: "mysql"]]
    vars << [wordpress: [version: "4.7.3-apache", image: "wordpress"]]
}
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.vars.size() == 2
                assert s.vars.mysql.size() == 2
                assert s.vars.mysql.version == '5.6'
            },
        ]
        doTest test
    }

    @Test
    void "vars_add"() {
        def test = [
            name: 'vars_add',
            script: '''
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app" with {
    vars << [mysql: [version: "5.6", image: "mysql"]]
    vars.mysql.putAll([name: "aaa"])
}
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.vars.size() == 1
                assert s.vars.mysql.size() == 3
                assert s.vars.mysql.version == '5.6'
                assert s.vars.mysql.name == 'aaa'
            },
        ]
        doTest test
    }

    @Test
    void "load manifests to destination directory in argument"() {
        def test = [
            script: '''
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app", dest: "/etc/kubernetes/addon"
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.vars.size() == 0
                assert s.destination == "/etc/kubernetes/addon"
            },
        ]
        doTest test
    }

    @Test
    void "load manifests to destination directory as statement"() {
        def test = [
            script: '''
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app" with {
    dest dir: "/etc/kubernetes/addon"
}
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.vars.size() == 0
                assert s.destination == "/etc/kubernetes/addon"
            },
        ]
        doTest test
    }

    @Test
    void "dry-run to see the generated templates"() {
        def test = [
            script: '''
service "k8s-cluster"
service "repo-git", group: "wordpress-app" with {
    remote url: "git@github.com:devent/wordpress-app.git"
    credentials "ssh", key: "id_rsa"
}
service "from-repository", repo: "wordpress-app", dryrun: true
''',
            before: { Map args ->
                def tmp = folder.newFolder()
                unzip FromRepositoryScriptTest.class.getResource("repo_only_app_zip.txt"), tmp
                args.tmpRepo = tmp
            },
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('from-repository').size() == 1
                FromRepository s = services.getServices('from-repository')[0]
                assert s.vars.size() == 0
                assert s.dryrun == true
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        test.before(test)
        def services = servicesFactory.create()
        services.targets.addTarget SshFactory.localhost(injector)
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'repo-git', gitFactory
        services.putAvailableService 'from-repository', fromRepositoryFactory
        robobeeScriptFactory.create folder.newFile(), test.script, test.scriptVars, services call()
        Closure expected = test.expected
        expected services
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    def injector

    @Before
    void setupTest() {
        toStringStyle
        injector = Guice.createInjector(
                new K8sModule(),
                new K8sClusterModule(),
                new FromRepositoryModule(),
                new GitRepoModule(),
                new PropertiesModule(),
                new DebugLoggingModule(),
                new TypesModule(),
                new StringsModule(),
                new HostServicesModule(),
                new TargetsModule(),
                new TargetsServiceModule(),
                new PropertiesUtilsModule(),
                new ResourcesModule(),
                new TlsModule(),
                new RobobeeScriptModule(),
                new SystemNameMappingsModule(),
                new HostServicePropertiesServiceModule(),
                )
        injector.injectMembers(this)
    }

    static void unzip(URL source, File dest) {
        def zip = new File(dest, "tmp.zip")
        IOUtils.copy source.openStream(), new FileOutputStream(zip)
        def zipFile = new ZipFile(zip)
        zipFile.extractAll(dest.absolutePath)
        zip.delete()
    }
}
