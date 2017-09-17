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
    KubeNodeClient(@Assisted ClusterHost cluster, @Assisted Object parent) {
        super(cluster, parent)
    }

    @AssistedInject
    KubeNodeClient(@Assisted NamespacedKubernetesClient client, @Assisted Object parent) {
        super(client, parent)
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
        kubeNodeResourceFactory.create client, r, parent node
    }

    KubeNodeResource getNodeResource(String label, String value) {
        def client = createClient()
        def r = client.nodes().withLabel(label, value)
        kubeNodeResourceFactory.create client, r, parent
    }

    KubeNodeResource getNodeResource(String name) {
        def client = createClient()
        def r = client.nodes().withName(name)
        kubeNodeResourceFactory.create client, r, parent
    }
}
