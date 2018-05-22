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
package com.anrisoftware.sscontrol.k8scluster.service.internal

import static com.anrisoftware.globalpom.utils.TestUtils.*

import javax.inject.Inject

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.k8scluster.service.external.ContextFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterFactory
import com.anrisoftware.sscontrol.k8scluster.service.external.K8sClusterHostFactory
import com.anrisoftware.sscontrol.k8scluster.service.internal.CredentialsAnonImpl.CredentialsAnonImplFactory
import com.anrisoftware.sscontrol.types.host.external.TargetHost

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class K8sClusterHostImplTest {

    @Inject
    K8sClusterHostFactory clusterHostFactory

    @Inject
    K8sClusterFactory clusterFactory

    @Inject
    CredentialsAnonImplFactory credentialsAnonFactory

    @Inject
    ContextFactory contextFactory

    @Test
    void "getUrl"() {
        def cluster = clusterFactory.create([:])
        def target = [
            getHost: {'localhost'},
            getHostAddress: {'127.0.0.1'},
            getPort: {22},
            getProto: {null},
        ] as TargetHost
        def credentials = credentialsAnonFactory.create([type: 'anon'])
        def host = clusterHostFactory.create(cluster, target, [credentials])
        host.proto = 'https'
        host.port = 443
        assert host.url.toString() == 'https://localhost:443'
    }

    @Before
    void setupTest() {
        toStringStyle
        K8sClusterModules.createInjector().injectMembers(this)
    }
}
