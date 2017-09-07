package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer
import com.google.inject.assistedinject.Assisted

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.api.model.DoneableNode
import io.fabric8.kubernetes.api.model.Node as KNode
import io.fabric8.kubernetes.api.model.NodeCondition
import io.fabric8.kubernetes.api.model.NodeFluent
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.Watch
import io.fabric8.kubernetes.client.dsl.Resource
import io.fabric8.kubernetes.client.internal.readiness.ReadinessWatcher

/**
 * Kubectl client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubectlClient {

    HostServiceScript script

    ClusterHost cluster

    @Inject
    UriBase64Renderer uriBase64Renderer

    NamespacedKubernetesClient client

    @Inject
    KubectlClient(@Assisted HostServiceScript script, @Assisted ClusterHost cluster) {
        this.script = script
        this.cluster = cluster
    }

    KubectlClient reuseClient(NamespacedKubernetesClient client) {
        this.client = client
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
        def client = createClient()
        def res = getNodeResource label, value
        def node = getNode res, label, value
        if (node.status.conditions.find({NodeCondition it -> it.type == 'Ready' && it.status == 'True'})) {
            return
        }
        def watcher = new ReadinessWatcher(node)
        Watch watch = res.watch(watcher)
        return watcher.await(amount, timeUnit)
    }

    def applyLabelToNode(String nodeLabel, String nodeValue, String key, String value) {
        def client = createClient()
        def res = getNodeResource(nodeLabel, nodeValue)
        def node = getNode res, nodeLabel, nodeValue
        res = getNodeResource node.metadata.name
        DoneableNode dn = res.edit()
        NodeFluent.MetadataNested m = dn.editOrNewMetadata()
        if (key.endsWith("-")) {
            key = key[0..(key.length() - 2)]
            log.debug 'Remove label {} to node {}', key, node.metadata.name
            m.removeFromLabels(key).endMetadata()
        } else {
            log.debug 'Add label {}={} to node {}', key, value, node.metadata.name
            m.addToLabels(key, value).endMetadata()
        }
        node = dn.done()
        res.patch(node)
    }

    KNode getNode(String label, String value) {
        def client = createClient()
        def res = getNodeResource(label, value)
        getNode res, label, value
    }

    KNode getNode(Resource res, String label, String value) {
        def client = createClient()
        io.fabric8.kubernetes.api.model.NodeList list = res.list()
        if (list.items.isEmpty()) {
            throw new NoResourceFoundException(this, label, value)
        }
        list.items[0]
    }

    Resource getNodeResource(String label, String value) {
        def client = createClient()
        client.nodes().withLabel(label, value)
    }

    Resource getNodeResource(String name) {
        def client = createClient()
        client.nodes().withName(name)
    }

    NamespacedKubernetesClient createClient() {
        try  {
            if (client) {
                return client
            }
            def config = buildConfig cluster.url, cluster.credentials
            def client = new AutoAdaptableKubernetesClient(config)
            log.debug 'Created client {}', client
            this.client = client
            return client
        } catch (Exception e) {
            throw new CreateClientException(this, e)
        }
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
}
