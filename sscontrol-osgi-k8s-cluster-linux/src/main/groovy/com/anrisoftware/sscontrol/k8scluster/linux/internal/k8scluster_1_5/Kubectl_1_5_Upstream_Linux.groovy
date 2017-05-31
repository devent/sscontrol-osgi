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
package com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5

import static com.anrisoftware.sscontrol.k8scluster.linux.internal.k8scluster_1_5.K8sCluster_1_5_Linux_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8sbase.upstream.external.Kubectl_1_5_Upstream

import groovy.util.logging.Slf4j

/**
 * Installs kubectl from the upstream sources for GNU/Linux.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Kubectl_1_5_Upstream_Linux extends Kubectl_1_5_Upstream {

    @Inject
    K8sCluster_1_5_Linux_Properties linuxPropertiesProvider

    @Override
    Object run() {
        installKubernetes()
    }

    @Override
    ContextProperties getDefaultProperties() {
        linuxPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
