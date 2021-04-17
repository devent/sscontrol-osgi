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
package com.anrisoftware.sscontrol.k8snode.script.debian.internal.k8snode_1_13.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * K8s-Node Debian properties provider from
 * {@code "/k8s_node_1_13_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sNodeDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sNodeDebianProperties.class.getResource("/k8s_node_1_13_debian_9.properties")

    K8sNodeDebianProperties() {
        super(K8sNodeDebianProperties.class, RESOURCE)
    }
}
