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
package com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sModule
import com.anrisoftware.sscontrol.k8sbase.base.internal.K8sPreModule
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterModule
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterPreModule
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterImpl.K8sClusterImplFactory
import com.anrisoftware.sscontrol.k8scluster.internal.K8sClusterPreScriptImpl.K8sClusterPreScriptImplFactory
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Factory
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Module
import com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Service
import com.anrisoftware.sscontrol.k8scluster.upstream.external.K8sCluster_1_5_Upstream_Module
import com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.service.MonitoringClusterHeapsterInfluxdbGrafanaModule
import com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.service.MonitoringClusterHeapsterInfluxdbGrafanaPreModule
import com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.service.MonitoringClusterHeapsterInfluxdbGrafanaImpl.MonitoringClusterHeapsterInfluxdbGrafanaImplFactory
import com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.service.MonitoringClusterHeapsterInfluxdbGrafanaPreScriptImpl.MonitoringClusterHeapsterInfluxdbGrafanaPreScriptImplFactory
import com.anrisoftware.sscontrol.runner.groovy.internal.RunnerModule
import com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImpl.RunScriptImplFactory
import com.anrisoftware.sscontrol.runner.test.external.AbstractRunnerTestBase
import com.anrisoftware.sscontrol.ssh.internal.SshModule
import com.anrisoftware.sscontrol.ssh.internal.SshPreModule
import com.anrisoftware.sscontrol.ssh.internal.SshImpl.SshImplFactory
import com.anrisoftware.sscontrol.ssh.internal.SshPreScriptImpl.SshPreScriptImplFactory
import com.anrisoftware.sscontrol.ssh.linux.external.Ssh_Linux_Factory
import com.anrisoftware.sscontrol.ssh.linux.internal.Ssh_Linux_Module
import com.anrisoftware.sscontrol.tls.internal.TlsModule
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService
import com.anrisoftware.sscontrol.types.external.HostServices
import com.google.inject.AbstractModule

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class MonitoringClusterHeapsterInfluxdbGrafana_1_5_ScriptTest extends AbstractRunnerTestBase {

    @Inject
    RunScriptImplFactory runnerFactory

    @Inject
    SshImplFactory sshFactory

    @Inject
    SshPreScriptImplFactory sshPreFactory

    @Inject
    Ssh_Linux_Factory ssh_Linux_Factory

    @Inject
    K8sClusterImplFactory clusterFactory

    @Inject
    K8sClusterPreScriptImplFactory clusterPreFactory

    @Inject
    K8sCluster_1_5_Linux_Factory cluster_1_5_Factory

    @Inject
    MonitoringClusterHeapsterInfluxdbGrafanaImplFactory monitoringClusterHeapsterInfluxdbGrafanaFactory

    @Inject
    MonitoringClusterHeapsterInfluxdbGrafanaPreScriptImplFactory monitoringClusterHeapsterInfluxdbGrafanaPreFactory

    @Inject
    MonitoringClusterHeapsterInfluxdbGrafana_1_5_Factory monitoringClusterHeapsterInfluxdbGrafana_1_5_Factory

    static final URL hostnameScript = MonitoringClusterHeapsterInfluxdbGrafana_1_5_ScriptTest.class.getResource('HostnameScript.groovy')

    @Test
    void "script_unsecured"() {
        def test = [
            name: "script_unsecured",
            script: """
service "ssh", host: "localhost"
service "k8s-cluster", target: 'default'
service "monitoring-cluster-heapster-influxdb-grafana", cluster: 'default'
""",
            generatedDir: folder.newFolder(),
            expectedServicesSize: 3,
            expected: { Map args ->
                File dir = args.dir
                File binDir = new File(dir, '/usr/local/bin')
                assertFileResource MonitoringClusterHeapsterInfluxdbGrafana_1_5_ScriptTest, binDir, "kubectl.out", "${args.test.name}_kubectl_expected.txt"
            },
        ]
        doTest test
    }

    @Before
    void checkProfile() {
        checkProfile LOCAL_PROFILE
    }

    def getRunScriptFactory() {
        runnerFactory
    }

    HostServices putServices(HostServices services) {
        services.putAvailableService 'ssh', sshFactory
        services.putAvailablePreService 'ssh', sshPreFactory
        services.putAvailableScriptService 'ssh-linux-0', ssh_Linux_Factory
        services.putAvailableService 'k8s-cluster', clusterFactory
        services.putAvailablePreService 'k8s-cluster', clusterPreFactory
        services.putAvailableScriptService 'k8s-cluster-linux-0', cluster_1_5_Factory
        services.putAvailableService 'monitoring-cluster-heapster-influxdb-grafana', monitoringClusterHeapsterInfluxdbGrafanaFactory
        services.putAvailablePreService 'monitoring-cluster-heapster-influxdb-grafana', monitoringClusterHeapsterInfluxdbGrafanaPreFactory
        services.putAvailableScriptService 'monitoring-cluster-heapster-influxdb-grafana-linux-0', monitoringClusterHeapsterInfluxdbGrafana_1_5_Factory
        return services
    }

    void createDummyCommands(File dir) {
        createDebianJessieCatCommand dir, 'cat'
        createEchoCommands dir, [
            'mkdir',
            'chown',
            'chmod',
            'sudo',
            'scp',
            'rm',
            'cp',
            'apt-get',
            'service',
            'id',
            'basename',
            'wget',
            'which',
            'sha256sum',
        ]
        def binDir = new File(dir, '/usr/local/bin')
        binDir.mkdirs()
        createEchoCommands binDir, ['kubectl']
    }

    List getAdditionalModules() {
        def modules = super.additionalModules
        modules << new RunnerModule()
        modules << new TlsModule()
        modules << new ResourcesModule()
        modules << new SshModule()
        modules << new SshPreModule()
        modules << new Ssh_Linux_Module()
        modules << new K8sModule()
        modules << new K8sPreModule()
        modules << new K8sClusterModule()
        modules << new K8sClusterPreModule()
        modules << new K8sCluster_1_5_Upstream_Module()
        modules << new K8sCluster_1_5_Linux_Module()
        modules << new MonitoringClusterHeapsterInfluxdbGrafanaModule()
        modules << new MonitoringClusterHeapsterInfluxdbGrafanaPreModule()
        modules << new MonitoringClusterHeapsterInfluxdbGrafana_1_5_Module()
        modules << new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind HostServiceScriptService.class to K8sCluster_1_5_Linux_Service.class
                    }
                }
    }


    @Before
    void setupTest() {
        toStringStyle
        injector = createInjector()
        injector.injectMembers(this)
        this.threads = createThreads()
    }
}
