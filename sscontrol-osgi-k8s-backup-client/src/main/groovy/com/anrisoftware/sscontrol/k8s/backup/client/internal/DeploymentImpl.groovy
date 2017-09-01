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
package com.anrisoftware.sscontrol.k8s.backup.client.internal

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.sscontrol.k8s.backup.client.external.CreateClientException
import com.anrisoftware.sscontrol.k8s.backup.client.external.Deployment
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentFactory
import com.anrisoftware.sscontrol.k8s.backup.client.external.DeploymentLogger
import com.anrisoftware.sscontrol.k8s.backup.client.external.GetDeploymentsException
import com.anrisoftware.sscontrol.k8s.backup.client.external.GetServicesException
import com.anrisoftware.sscontrol.k8s.backup.client.external.WaitScalingTimeoutException
import com.anrisoftware.sscontrol.k8s.backup.client.external.WaitScalingUnexpectedException
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterHost
import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer
import com.google.inject.assistedinject.Assisted
import com.google.inject.assistedinject.AssistedInject

import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.dsl.ExecWatch
import io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation
import io.fabric8.kubernetes.client.dsl.internal.PodOperationsImpl

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class DeploymentImpl implements Deployment {

    @Inject
    DeploymentLogger log

    @Inject
    UriBase64Renderer uriBase64Renderer

    @Inject
    DeploymentFactory deploymentFactory

    private final NamespacedKubernetesClient client

    private final K8sClusterHost host

    @Inject
    @AssistedInject
    DeploymentImpl(@Assisted K8sClusterHost host) {
        this.host = host
        this.client = null
    }

    @Inject
    @AssistedInject
    DeploymentImpl(@Assisted K8sClusterHost host, @Assisted NamespacedKubernetesClient client) {
        this.host = host
        this.client = client
    }

    @Override
    Deployment createClient() {
        try  {
            def config = buildConfig host.url, host.credentials
            def client = new AutoAdaptableKubernetesClient(config)
            log.createdClient host.url
            return deploymentFactory.create(host, client)
        } catch (Exception e) {
            throw new CreateClientException(this, e)
        }
    }

    @Override
    def getDeployment(String namespace, String name) {
        try  {
            return client.extensions().deployments().inNamespace(namespace).withName(name)
        } catch (Exception e) {
            throw new GetDeploymentsException(this, namespace, name, e)
        }
    }

    @Override
    def getService(String namespace, String name) {
        try  {
            return client.services().inNamespace(namespace).withName(name)
        } catch (Exception e) {
            throw new GetServicesException(this, namespace, name, e)
        }
    }

    @Override
    void scaleDeployment(Object deployOp, int replicas) {
        scaleDeployment deployOp, replicas, true
    }

    @Override
    void scaleDeployment(Object deployOp, int replicas, boolean deleteOnError) {
        deployOp = deployOp as HasMetadataOperation
        deployOp.scale replicas, true
        if (replicas > 0) {
            waitScaleUp deployOp, deleteOnError
        } else {
            waitScaleZero deployOp
        }
        log.scaledDeployment deployOp, replicas
    }

    @Override
    void waitScaleUp(Object deployOp, boolean deleteOnError) {
        def waitTime = Duration.standardHours 1
        try {
            deployOp = deployOp as HasMetadataOperation
            deployOp.waitUntilReady waitTime.standardSeconds, TimeUnit.SECONDS
        } catch (e) {
            def deploy = deployOp.get()
            def ex = new WaitScalingTimeoutException(e, deploy.metadata.namespace, deploy.metadata.name, 0, waitTime)
            if (deleteOnError) {
                scaleDeployment deployOp, 0
            }
            throw ex
        }
    }

    @Override
    void waitScaleZero(Object deployOp) {
        deployOp = deployOp as HasMetadataOperation
        def deploy = deployOp.get()
        def namespace = deploy.metadata.namespace
        def name = deploy.metadata.name
        def countDown = new CountDownLatch(1)
        def waitTime = Duration.standardMinutes 5
        def ex = null
        Thread.start {
            try {
                while (getPods(namespace, name).size() > 0) {
                    def pods = getPods(namespace, name)
                    Thread.sleep 1000
                }
            }
            catch(e) {
                ex = e
            }
            finally {
                countDown.countDown()
            }
        }
        if (!countDown.await(waitTime.standardSeconds, TimeUnit.SECONDS)) {
            throw new WaitScalingTimeoutException(deploy.metadata.namespace, deploy.metadata.name, 0, waitTime)
        }
        if (ex) {
            throw new WaitScalingUnexpectedException(ex,deploy.metadata.namespace, deploy.metadata.name, 0)
        }
    }

    @Override
    List<?> getPods(String namespace, String name) {
        client.pods().inNamespace(namespace).withLabel("app", name).list().items
    }

    @Override
    def createPublicService(Object deployOp) {
        deployOp = deployOp as HasMetadataOperation
        String namespace = deployOp.get().metadata.namespace
        String name = deployOp.get().metadata.name
        String serviceName = "${name}-public"
        def service = getService namespace, serviceName get()
        if (service != null) {
            return service
        }
        service = client.services().inNamespace(namespace).createNew()
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

    @Override
    void deleteService(Object service) {
        service = service as Service
        String namespace = service.metadata.namespace
        String name = service.metadata.name
        client.services().inNamespace(namespace).withName(name).delete()
        log.deleteService namespace, name
    }

    @Override
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

    @Override
    List<?> waitDeploy(Object deployOp, int replicas, boolean ready) {
        deployOp = deployOp as HasMetadataOperation
        String namespace = deployOp.get().metadata.namespace
        String name = deployOp.get().metadata.name
        client.pods().inNamespace(namespace).withLabel("app", name).list().items
    }

    void execCommand(Object deployOp, String cmd) {
        deployOp = deployOp as HasMetadataOperation
        Pod pod = getPods(deployOp.namespace, deployOp.name)[0]
        PodOperationsImpl podOp = client.pods().inNamespace(deployOp.namespace).withName(pod.metadata.name)
        ExecWatch watch = podOp.redirectingInput().redirectingOutput().exec(cmd)
        log.commandExecuted(deployOp, cmd)
    }
}
