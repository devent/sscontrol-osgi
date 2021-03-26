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
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Docker CE 18 Debian 9</i> properties provider from
 * {@code "/dockerce_18_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Dockerce_18_Debian_9_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Dockerce_18_Debian_9_Properties.class.getResource("/dockerce_18_debian_9.properties")

    Dockerce_18_Debian_9_Properties() {
        super(Dockerce_18_Debian_9_Properties.class, RESOURCE)
    }
}
