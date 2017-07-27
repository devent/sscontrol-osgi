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
package com.anrisoftware.sscontrol.k8s.backup.script.internal.script_1_7

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

import org.junit.Before
import org.junit.Test

import groovy.util.logging.Slf4j
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient

/**
 * Tests fabric8io/kubernetes-client.
 * https://github.com/fabric8io/kubernetes-client
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class KubernetesClientTest {

    @Test
    void "get deployments"() {
        def client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl("https://andrea-master-0.muellerpublic.de").build())
        System.out.println(
                client.namespaces().list()
                )

        System.out.println(
                client.namespaces().withLabel("this", "works").list()
                )

        System.out.println(
                client.pods().withLabel("this", "works").list()
                )

        System.out.println(
                client.pods().inNamespace("test").withLabel("this", "works").list()
                )

        System.out.println(
                client.pods().inNamespace("test").withName("testing").get()
                )
    }

    @Before
    void beforeMethod() {
        assumeSocketExists '/tmp/robobee@andrea-master-0.muellerpublic.de:22'
    }
}
