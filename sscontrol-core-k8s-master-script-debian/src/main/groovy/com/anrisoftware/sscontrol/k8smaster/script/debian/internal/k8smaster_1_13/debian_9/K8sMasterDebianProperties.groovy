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
package com.anrisoftware.sscontrol.k8smaster.script.debian.internal.k8smaster_1_13.debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * K8s-Master Debian properties provider from
 * {@code "/k8s_master_1_13_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sMasterDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sMasterDebianProperties.class.getResource("/k8s_master_1_13_debian_9.properties")

    K8sMasterDebianProperties() {
        super(K8sMasterDebianProperties.class, RESOURCE)
    }
}
