package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import com.google.inject.assistedinject.Assisted

import io.fabric8.kubernetes.api.model.NodeList as KNodeList
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.Watch
import io.fabric8.kubernetes.client.dsl.Resource
import io.fabric8.kubernetes.client.internal.readiness.ReadinessWatcher

/**
 * Kube node resource.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
class KubeNodeResource {

    final NamespacedKubernetesClient client

    final Resource resource

    final Object parent

    @Inject
    KubeNodeFactory nodeFactory

    @Inject
    KubeNodeResource(@Assisted NamespacedKubernetesClient client,
    @Assisted Resource resource,
    @Assisted Object parent) {
        this.client = client
        this.resource = resource
        this.parent = parent
    }

    KubeNode getNode() {
        KNodeList list = resource.list()
        if (list.items.isEmpty()) {
            throw new NoResourceFoundException(this, resource.labels)
        }
        def node = list.items[0]
        nodeFactory.create(client, this, node, parent)
    }

    void waitNodeReady(def node, long amount, TimeUnit timeUnit) {
        def watcher = new ReadinessWatcher(node)
        Watch watch = resource.watch(watcher)
        watcher.await(amount, timeUnit)
    }
}
