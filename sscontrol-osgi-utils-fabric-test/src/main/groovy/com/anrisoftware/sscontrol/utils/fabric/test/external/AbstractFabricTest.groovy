package com.anrisoftware.sscontrol.utils.fabric.test.external

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule

import com.anrisoftware.sscontrol.k8scluster.service.external.Context
import com.anrisoftware.sscontrol.k8scluster.service.external.ContextFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sCluster
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterHostFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.CredentialsCertImpl.CredentialsCertImplFactory
import com.anrisoftware.sscontrol.ssh.service.internal.SshHostImpl.SshHostImplFactory
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.types.host.external.TargetHost
import com.google.inject.Injector

import io.fabric8.kubernetes.api.model.StatusBuilder
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractFabricTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(false)

    @Inject
    K8sClusterHostFactory clusterHostFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    SshHostImplFactory sshHostFactory

    @Inject
    CredentialsCertImplFactory credentialsCertFactory

    @Inject
    ContextFactory contextFactory

    Injector injector

    @Before
    void setupTest() {
        toStringStyle
        this.injector = FabricTestModules.createInjector().createChildInjector(additionalModules)
        injector.injectMembers(this)
    }

    List getAdditionalModules() {
        []
    }

    /**
     * Creates the cluster host from the Kubernetes server host.
     */
    ClusterHost createClusterHost(String host) {
        def cb = new ConfigBuilder().withMasterUrl(host).build()
        def client = new AutoAdaptableKubernetesClient(cb)
        def config = client.configuration
        TargetHost target = sshHostFactory.create host: client.masterUrl
        List<TargetHost> targets = [target]
        K8sCluster cluster = clusterFactory.create targets: targets
        cluster.cluster name: 'default-context'
        cluster.context name: 'default-context'
        if (config.clientCertFile) {
            cluster.credentials type: 'cert', cert: config.clientCertFile, key: config.clientKeyFile
        } else {
            cluster.credentials type: 'anon'
        }
        Credentials credentials = cluster.credentials[0]
        Context context = contextFactory.create name: 'default-context'
        clusterHostFactory.create cluster, target, credentials, context
    }

    /**
     * Creates the cluster host from the Kubernetes server.
     */
    ClusterHost createClusterHost() {
        TargetHost target = sshHostFactory.create host: server.client.masterUrl
        List<TargetHost> targets = [target]
        K8sCluster cluster = clusterFactory.create targets: targets
        cluster.cluster name: 'default-context'
        cluster.context name: 'default-context'
        cluster.credentials type: 'anon'
        Credentials credentials = cluster.credentials[0]
        Context context = contextFactory.create name: 'default-context'
        clusterHostFactory.create cluster, target, credentials, context
    }

    def createTestNode() {
        server.expect().get().withPath("/api/v1/nodes").andReturn(200, new StatusBuilder().build()).always()
        server.expect().withPath("/api/v1/nodes/node0").andReturn(200,
                new io.fabric8.kubernetes.api.model.NodeBuilder()
                .withNewMetadata()
                .withName("node0")
                .withResourceVersion("1")
                .endMetadata()
                .withNewStatus()
                .addNewCondition()
                .withType("Ready")
                .withStatus("True")
                .endCondition()
                .endStatus()
                .build()
                ).always()
    }

    def createMariadbDeploy() {
        server.expect().withPath("/apis/extensions/v1beta1/namespaces/test/deployments/mariadb").
                andReturn(200, new DeploymentBuilder().withNewMetadata()
                .withName("mariadb")
                .addToLabels("app", "mariadb")
                .withResourceVersion("1")
                .withGeneration(1L)
                .endMetadata()
                .withNewSpec()
                .withNewSelector()
                .addToMatchLabels("app", "mariadb")
                .endSelector()
                .withReplicas(1)
                .endSpec()
                .build()).once()
    }
}
