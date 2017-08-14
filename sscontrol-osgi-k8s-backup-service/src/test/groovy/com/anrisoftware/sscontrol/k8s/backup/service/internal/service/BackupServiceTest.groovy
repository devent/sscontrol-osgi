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
package com.anrisoftware.sscontrol.k8s.backup.service.internal.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.joda.time.Duration
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.core.durationformat.DurationFormatModule
import com.anrisoftware.globalpom.core.durationsimpleformat.DurationSimpleFormatModule
import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.globalpom.core.strings.StringsModule
import com.anrisoftware.propertiesutils.PropertiesUtilsModule
import com.anrisoftware.sscontrol.debug.internal.DebugLoggingModule
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupModule
import com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImpl.BackupImplFactory
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sModule
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterModule
import com.anrisoftware.sscontrol.properties.internal.PropertiesModule
import com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImpl.HostServicePropertiesImplFactory
import com.anrisoftware.sscontrol.services.internal.host.HostServicesModule
import com.anrisoftware.sscontrol.services.internal.host.HostServicesImpl.HostServicesImplFactory
import com.anrisoftware.sscontrol.services.internal.ssh.TargetsImpl.TargetsImplFactory
import com.anrisoftware.sscontrol.services.internal.targets.TargetsModule
import com.anrisoftware.sscontrol.services.internal.targets.TargetsServiceModule
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScriptModule
import com.anrisoftware.sscontrol.shell.external.utils.SshFactory
import com.anrisoftware.sscontrol.shell.external.utils.RobobeeScript.RobobeeScriptFactory
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.host.external.HostPropertiesService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.anrisoftware.sscontrol.types.misc.internal.TypesModule
import com.anrisoftware.sscontrol.types.ssh.external.TargetsService
import com.anrisoftware.sscontrol.utils.systemmappings.internal.SystemNameMappingsModule
import com.google.inject.AbstractModule
import com.google.inject.Guice

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class BackupServiceTest {

    @Inject
    RobobeeScriptFactory robobeeScriptFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    BackupImplFactory serviceFactory

    @Inject
    HostServicesImplFactory servicesFactory

    @Test
    void "dest_dir_client"() {
        def test = [
            name: 'dest_dir_client',
            script: '''
service "k8s-cluster"
service "backup" with {
    service namespace: "wordpress", name: "db"
    destination dir: "/mnt/backup"
    client key: "id_rsa", config: """
ProxyCommand ssh -o ControlMaster=auto -o ControlPath=/tmp/robobee@%h:22 robobee@node nc %h %p
"""
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('backup').size() == 1
                Backup s = services.getServices('backup')[0]
                assert s.service.namespace == 'wordpress'
                assert s.target.host == 'localhost'
                assert s.cluster.host == 'localhost'
                assert s.service.name == 'db'
                assert s.destination.dest.toString() == 'file:/mnt/backup'
                assert s.client.config =~ 'ProxyCommand'
                assert s.client.key.toString() == 'file:id_rsa'
            },
        ]
        doTest test
    }

    @Test
    void "dest_service_source"() {
        def test = [
            name: 'dest_service_source',
            script: '''
service "k8s-cluster"
service "backup" with {
    service namespace: "wordpress", name: "db", source: "/conf/config"
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('backup').size() == 1
                Backup s = services.getServices('backup')[0]
                assert s.service.namespace == 'wordpress'
                assert s.service.name == 'db'
                assert s.service.source == "/conf/config"
            },
        ]
        doTest test
    }

    @Test
    void "dest_dir_proxy"() {
        def test = [
            name: 'dest_dir_proxy',
            script: '''
service "k8s-cluster"
service "backup" with {
    service namespace: "wordpress", name: "db"
    destination dir: "/mnt/backup"
    client key: "id_rsa", proxy: true
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('backup').size() == 1
                Backup s = services.getServices('backup')[0]
                assert s.service.namespace == 'wordpress'
                assert s.target.host == 'localhost'
                assert s.cluster.host == 'localhost'
                assert s.service.name == 'db'
                assert s.destination.dest.toString() == 'file:/mnt/backup'
                assert s.client.key.toString() == 'file:id_rsa'
                assert s.client.proxy == true
            },
        ]
        doTest test
    }

    @Test
    void "timeout_duration"() {
        def test = [
            name: 'timeout_duration',
            script: '''
service "k8s-cluster"
service "backup" with {
    client timeout: "PT1H"
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('backup').size() == 1
                Backup s = services.getServices('backup')[0]
                assert s.target.host == 'localhost'
                assert s.cluster.host == 'localhost'
                assert s.client.timeout == Duration.standardMinutes(60)
            },
        ]
        doTest test
    }

    @Test
    void "timeout_simple"() {
        def test = [
            name: 'timeout_simple',
            script: '''
service "k8s-cluster"
service "backup" with {
    client timeout: "1h"
}
''',
            scriptVars: [:],
            expected: { HostServices services ->
                assert services.getServices('backup').size() == 1
                Backup s = services.getServices('backup')[0]
                assert s.target.host == 'localhost'
                assert s.cluster.host == 'localhost'
                assert s.client.timeout == Duration.standardMinutes(60)
            },
        ]
        doTest test
    }

    void doTest(Map test) {
        log.info '\n######### {} #########\ncase: {}', test.name, test
        def services = servicesFactory.create()
        services.targets.addTarget SshFactory.localhost(injector)
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailableService 'backup', serviceFactory
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
                new BackupModule(),
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
                new DurationFormatModule(),
                new DurationSimpleFormatModule(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind TargetsService to TargetsImplFactory
                        bind(HostPropertiesService).to(HostServicePropertiesImplFactory)
                    }
                })
        injector.injectMembers(this)
    }
}
