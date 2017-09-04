package com.anrisoftware.sscontrol.utils.fabric.test.external

import static okhttp3.TlsVersion.TLS_1_0

import org.junit.rules.ExternalResource

import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer
import io.fabric8.mockwebserver.dsl.MockServerExpectation
import okhttp3.mockwebserver.MockWebServer

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class KubernetesServer extends ExternalResource {

    KubernetesMockServer mock

    NamespacedKubernetesClient client

    final boolean https

    KubernetesServer() {
        this(true)
    }

    KubernetesServer(boolean https) {
        this.https = https
    }

    @Override
    void before() {
        mock = new KubernetesMockServer(https) {
                    NamespacedKubernetesClient createClient() {
                        def config = new ConfigBuilder()
                                .withMasterUrl(url("/"))
                                .withNamespace("test")
                        if (https) {
                            config.withTrustCerts(https).withTlsVersions(TLS_1_0)
                        }
                        config = config.build()
                        return new DefaultKubernetesClient(config)
                    }
                }
        mock.init()
        client = mock.createClient()
    }

    @Override
    void after() {
        mock.destroy()
        client.close()
    }

    NamespacedKubernetesClient getClient() {
        return client
    }

    MockServerExpectation expect() {
        return mock.expect()
    }

    MockWebServer getMockServer() {
        return mock.getServer()
    }
}
