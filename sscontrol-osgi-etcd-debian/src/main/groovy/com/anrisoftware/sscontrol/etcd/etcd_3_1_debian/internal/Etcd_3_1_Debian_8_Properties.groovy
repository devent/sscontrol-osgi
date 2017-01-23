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
package com.anrisoftware.sscontrol.etcd.etcd_3_1_debian.internal;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Etcd 3.1 Debian 8</i> properties provider from
 * {@code "/etcd_3_1_debian_8.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Etcd_3_1_Debian_8_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Etcd_3_1_Debian_8_Properties.class.getResource("/etcd_3_1_debian_8.properties");

    Etcd_3_1_Debian_8_Properties() {
        super(Etcd_3_1_Debian_8_Properties.class, RESOURCE);
    }
}
