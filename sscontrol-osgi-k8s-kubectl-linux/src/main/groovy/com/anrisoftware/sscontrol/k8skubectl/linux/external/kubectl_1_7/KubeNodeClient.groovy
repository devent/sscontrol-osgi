package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.google.inject.assistedinject.Assisted
import com.google.inject.assistedinject.AssistedInject

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.client.NamespacedKubernetesClient

/**
 * Kube node client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubeNodeClient extends KubectlClient {

    @Inject
    KubeNodeResourceFactory kubeNodeResourceFactory

    @AssistedInject
    KubeNodeClient(@Assisted ClusterHost cluster) {
        super(cluster)
    }

    @AssistedInject
    KubeNodeClient(@Assisted NamespacedKubernetesClient client) {
        super(client)
    }

    /**
     * Waits until the node is ready. The node is identified with the specified
     * label and value.
     */
    def waitNodeReady(String label, String value, Duration timeout) {
        waitNodeReady label, value, timeout.standardSeconds, TimeUnit.SECONDS
    }

    /**
     * Waits until the node is ready. The node is identified with the specified
     * label and value.
     */
    def waitNodeReady(String label, String value, long amount, TimeUnit timeUnit) {
        log.info 'Wait for node to be ready: {}', label
        def res = getNodeResource label, value
        def node = res.node
        node.waitNodeReady amount, timeUnit
    }

    def applyLabelToNode(String nodeLabel, String nodeValue, String key, String value) {
        def res = getNodeResource(nodeLabel, nodeValue)
        def node = res.node
        node.applyLabelToNode nodeLabel, nodeValue, key, value == null ? "null" : value
    }

    KubeNode getNode(String label, String value) {
        def client = createClient()
        def r = client.nodes().withLabel(label, value)
        kubeNodeResourceFactory.create client, r node
    }

    KubeNodeResource getNodeResource(String label, String value) {
        def client = createClient()
        def r = client.nodes().withLabel(label, value)
        kubeNodeResourceFactory.create client, r
    }

    KubeNodeResource getNodeResource(String name) {
        def client = createClient()
        def r = client.nodes().withName(name)
        kubeNodeResourceFactory.create client, r
    }
}
