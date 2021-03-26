/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.k8scluster.script.linux.internal.k8scluster_1_13

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>K8s-Cluster properties provider from
 * {@code "/k8s_cluster_1_13_linux.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sClusterLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sClusterLinuxProperties.class.getResource("/k8s_cluster_1_13_linux.properties")

    K8sClusterLinuxProperties() {
        super(K8sClusterLinuxProperties.class, RESOURCE)
    }
}
