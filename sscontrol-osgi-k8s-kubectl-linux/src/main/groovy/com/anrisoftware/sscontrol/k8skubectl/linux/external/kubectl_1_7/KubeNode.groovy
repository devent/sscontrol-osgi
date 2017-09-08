package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

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

    @Inject
    KubeNodeClientFactory nodeClientFactory

    @Inject
    KubeNode(@Assisted NamespacedKubernetesClient client, @Assisted KubeNodeResource resource, @Assisted KNode node) {
        this.client = client
        this.resource = resource
        this.node = node
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
        def res = nodeClientFactory.create client getNodeResource node.metadata.name
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
