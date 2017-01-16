/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>K8s-Master 1.5 Debian 8</i> properties provider from
 * {@code "/k8s_master_1_5_debian_8.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class K8sMaster_1_5_Debian_8_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = K8sMaster_1_5_Debian_8_Properties.class.getResource("/k8s_master_1_5_debian_8.properties");

    K8sMaster_1_5_Debian_8_Properties() {
        super(K8sMaster_1_5_Debian_8_Properties.class, RESOURCE);
    }
}
