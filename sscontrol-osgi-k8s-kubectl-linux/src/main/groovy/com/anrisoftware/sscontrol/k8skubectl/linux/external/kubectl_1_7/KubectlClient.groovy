package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.sscontrol.tls.external.Tls
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost
import com.anrisoftware.sscontrol.types.cluster.external.Credentials
import com.anrisoftware.sscontrol.utils.st.base64renderer.external.UriBase64Renderer
import com.google.inject.assistedinject.Assisted
import com.google.inject.assistedinject.AssistedInject

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.NamespacedKubernetesClient

/**
 * Kubectl client.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class KubectlClient {

    ClusterHost cluster

    Object parent

    @Inject
    UriBase64Renderer uriBase64Renderer

    NamespacedKubernetesClient client

    @AssistedInject
    KubectlClient(@Assisted ClusterHost cluster, @Assisted Object parent) {
        this.cluster = cluster
        this.parent = parent
    }

    @AssistedInject
    KubectlClient(@Assisted NamespacedKubernetesClient client, @Assisted Object parent) {
        this.client = client
        this.parent = parent
    }

    KubectlClient reuseClient(NamespacedKubernetesClient client) {
        this.client = client
    }

    NamespacedKubernetesClient createClient() {
        if (client) {
            return client
        }
        try  {
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
        Duration timeout = parent.kubectlTimeout
        config.withWebsocketTimeout timeout.millis
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
