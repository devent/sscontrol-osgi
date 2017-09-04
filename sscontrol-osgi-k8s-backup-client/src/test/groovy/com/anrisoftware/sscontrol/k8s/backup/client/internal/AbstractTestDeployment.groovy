package com.anrisoftware.sscontrol.k8s.backup.client.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Rule

import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.Context
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sCluster
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.ContextImpl.ContextImplFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.CredentialsCertImpl.CredentialsCertImplFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.K8sClusterHostImpl.K8sClusterHostImplFactory
import com.anrisoftware.sscontrol.ssh.service.internal.SshHostImpl.SshHostImplFactory
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.types.host.external.TargetHost

import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTestDeployment {

    @Rule
    public KubernetesServer server = new KubernetesServer(false)

    @Inject
    DeploymentFactory deploymentFactory

    @Inject
    K8sClusterHostImplFactory clusterHostFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    SshHostImplFactory sshHostFactory

    @Inject
    CredentialsCertImplFactory credentialsCertFactory

    @Inject
    ContextImplFactory contextFactory

    @Before
    void setupTest() {
        toStringStyle
        BackupClientTestModules.createInjector().injectMembers(this)
    }

    Deployment createDeployment() {
        TargetHost target = sshHostFactory.create host: server.client.masterUrl
        List<TargetHost> targets = [target]
        K8sCluster cluster = clusterFactory.create targets: targets
        cluster.cluster name: 'default-context'
        cluster.context name: 'default-context'
        cluster.credentials type: 'anon'
        Credentials credentials = cluster.credentials[0]
        Context context = contextFactory.create name: 'default-context'
        def host = clusterHostFactory.create cluster, target, credentials, context
        deploymentFactory.create host createClient()
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
