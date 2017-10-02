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
package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_8

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import com.google.inject.assistedinject.Assisted

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.api.model.DoneableNode
import io.fabric8.kubernetes.api.model.Node as KNode
import io.fabric8.kubernetes.api.model.NodeCondition
import io.fabric8.kubernetes.api.model.NodeFluent
import io.fabric8.kubernetes.client.NamespacedKubernetesClient

/**
 * Kube node.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubeNode {

    final NamespacedKubernetesClient client

    final KubeNodeResource resource

    final KNode node

    final Object parent

    @Inject
    KubeNodeClientFactory nodeClientFactory

    @Inject
    KubeNode(@Assisted NamespacedKubernetesClient client,
    @Assisted KubeNodeResource resource,
    @Assisted KNode node,
    @Assisted Object parent) {
        this.client = client
        this.resource = resource
        this.node = node
        this.parent = parent
    }

    /**
     * Waits until the node is ready. The node is identified with the specified
     * label and value.
     */
    void waitNodeReady(long amount, TimeUnit timeUnit) {
        if (node.status.conditions.find({ NodeCondition it ->
            it.type == 'Ready' && it.status == 'True'
        })) {
            return
        }
        resource.waitNodeReady node, amount, timeUnit
    }

    def applyLabelToNode(String nodeLabel, String nodeValue, String key, String value) {
        def res = nodeClientFactory.create client, parent getNodeResource node.metadata.name
        DoneableNode dn = res.resource.edit()
        NodeFluent.MetadataNested m = dn.editOrNewMetadata()
        if (key.endsWith("-")) {
            key = key[0..(key.length() - 2)]
            log.debug 'Remove label {} to node {}', key, node.metadata.name
            m.removeFromLabels(key).endMetadata()
        } else {
            log.debug 'Add label {}={} to node {}', key, value, node.metadata.name
            m.addToLabels(key, value).endMetadata()
        }
        res.resource.patch dn.done()
    }
}
