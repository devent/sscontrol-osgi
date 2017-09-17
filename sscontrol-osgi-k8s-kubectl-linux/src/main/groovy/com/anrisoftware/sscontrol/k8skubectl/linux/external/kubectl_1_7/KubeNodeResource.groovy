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
