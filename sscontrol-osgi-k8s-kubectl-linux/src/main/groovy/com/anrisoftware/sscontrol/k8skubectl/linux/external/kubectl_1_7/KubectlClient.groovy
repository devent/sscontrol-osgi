package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.types.host.external.HostServiceScript
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer
import com.google.inject.assistedinject.Assisted

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.Watch
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

    @Inject
    KubectlClient(@Assisted HostServiceScript script, @Assisted ClusterHost cluster) {
        this.script = script
        this.cluster = cluster
    }

    /**
     * Waits until the node is ready. The node is identified with the specified
     * label and value.
     */
    def waitNodeReady(String label, String value, long amount, TimeUnit timeUnit) {
        log.info 'Wait for node to be ready: {}', label
        def client = createClient()
        def res = client.nodes().withLabel(label, value)
        def node = res.get()
        def watcher = new ReadinessWatcher(node)
        Watch watch = res.watch(watcher)
        return watcher.await(amount, timeUnit)
    }

    NamespacedKubernetesClient createClient() {
        try  {
            def config = buildConfig cluster.url, cluster.credentials
            def client = new AutoAdaptableKubernetesClient(config)
            log.debug 'Created client {}', client
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
