package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import com.anrisoftware.sscontrol.k8s.backup.script.external.script_1_7.CreateClientException
import com.anrisoftware.sscontrol.k8s.backup.script.external.script_1_7.GetDeploymentsException
import com.anrisoftware.sscontrol.k8s.backup.script.external.script_1_7.GetServicesException
import com.anrisoftware.sscontrol.k8s.backup.service.external.Backup
import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterHost
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer
import com.google.inject.assistedinject.Assisted

import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Deployment {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface DeploymentFactory {

        Deployment create(Backup service)
    }

    @Inject
    @Assisted
    Backup service

    @Inject
    DeploymentLogger log

    @Inject
    UriBase64Renderer uriBase64Renderer

    NamespacedKubernetesClient k8sclient

    void createClient() {
        Backup service = service
        K8sClusterHost host = service.cluster
        try  {
            def config = buildConfig host.url, host.credentials
            this.k8sclient = new AutoAdaptableKubernetesClient(config)
            log.createdClient host.url
        } catch (Exception e) {
            throw new CreateClientException(this, e)
        }
    }

    def getDeployment(String name) {
        Backup service = service
        String namespace = service.service.namespace
        try  {
            return k8sclient.extensions().deployments().inNamespace(namespace).withName(name)
        } catch (Exception e) {
            throw new GetDeploymentsException(this, namespace, name, e)
        }
    }

    def getService(String namespace, String name) {
        try  {
            return k8sclient.services().inNamespace(namespace).withName(name)
        } catch (Exception e) {
            throw new GetServicesException(this, namespace, name, e)
        }
    }

    def scaleDeployment(HasMetadataOperation deploy, int replicas) {
        deploy.scale replicas, true
        if (replicas > 0) {
            deploy.waitUntilReady 15, TimeUnit.MINUTES
        }
        log.scaledDeployment deploy, replicas
    }

    Service createPublicService(HasMetadataOperation deploy) {
        String namespace = deploy.get().metadata.namespace
        String name = deploy.get().metadata.name
        String serviceName = "${name}-public"
        def service = getService namespace, serviceName get()
        if (service != null) {
            return service
        }
        service = k8sclient.services().inNamespace(namespace).createNew()
                .withNewMetadata()
                .withName(serviceName)
                .endMetadata()
                .withNewSpec()
                .withSelector([app: name])
                .withType("NodePort")
                .addNewPort().withPort(2222)
                .withNewTargetPort().withIntVal(2222)
                .endTargetPort()
                .endPort()
                .endSpec()
                .done()
        log.createPublicService namespace, serviceName
        return service
    }

    def deleteService(Service service) {
        String namespace = service.metadata.namespace
        String name = service.metadata.name
        k8sclient.services().inNamespace(namespace).withName(name).delete()
        log.deleteService namespace, name
    }

    def buildConfig(URI hostUrl, Credentials credentials) {
        def config = new ConfigBuilder().withMasterUrl(hostUrl.toString())
        if (credentials.hasProperty('tls')) {
            Tls tls = credentials.tls
            if (tls.ca) {
                config.withCaCertData uriBase64Renderer.toString(tls.ca, "base64", null)
            }
            if (tls.cert) {
                config.withClientCertData uriBase64Renderer.toString(tls.cert, "base64", null)
            }
            if (tls.key) {
                config.withClientKeyData uriBase64Renderer.toString(tls.key, "base64", null)
            }
        }
        return config.build()
    }

    def waitDeploy(HasMetadataOperation deploy, int replicas, boolean ready) {
        String namespace = deploy.get().metadata.namespace
        String name = deploy.get().metadata.name
        k8sclient.pods().inNamespace(namespace).withLabel("app", name).list().getItems()
    }
}
