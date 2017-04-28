package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before

import com.anrisoftware.globalpom.core.resources.ResourcesModule
import com.anrisoftware.sscontrol.k8s.fromreposiroty.service.internal.script_1_5.FromRepository_1_5_Factory
import com.anrisoftware.sscontrol.k8s.fromreposiroty.service.internal.script_1_5.FromRepository_1_5_Module
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
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService
import com.anrisoftware.sscontrol.types.host.external.HostServices
import com.google.inject.AbstractModule

/**
 *
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
abstract class Abstract_1_5_ScriptTest extends AbstractRunnerTestBase {

    static final URL kubectlCommand = Abstract_1_5_ScriptTest.class.getResource('kubectl_command.txt')

    static final URL certCaPem = Abstract_1_5_ScriptTest.class.getResource('cert_ca.txt')

    static final URL certCertPem = Abstract_1_5_ScriptTest.class.getResource('cert_cert.txt')

    static final URL certKeyPem = Abstract_1_5_ScriptTest.class.getResource('cert_key.txt')

    static final Map andreaLocalCerts = [
        worker: [
            ca: Abstract_1_5_ScriptTest.class.getResource('andrea_local_k8smaster_ca_cert.pem'),
            cert: Abstract_1_5_ScriptTest.class.getResource('andrea_local_node_0_robobee_test_cert.pem'),
            key: Abstract_1_5_ScriptTest.class.getResource('andrea_local_node_0_test_key_insecure.pem'),
        ],
    ]

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
    FromRepository_1_5_Factory monitoringClusterHeapsterInfluxdbGrafana_1_5_Factory

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
        modules << new FromRepository_1_5_Module()
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
