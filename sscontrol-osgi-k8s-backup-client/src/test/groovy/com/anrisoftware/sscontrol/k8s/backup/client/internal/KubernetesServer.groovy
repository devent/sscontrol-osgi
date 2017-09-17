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
package com.anrisoftware.sscontrol.k8s.backup.client.internal

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
