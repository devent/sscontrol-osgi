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
package com.anrisoftware.sscontrol.registry.docker.script.linux.internal.linux

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Docker</i> properties provider from
 * {@code "/docker_registry_debian_8.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DockerRegistryLinuxProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = DockerRegistryLinuxProperties.class.getResource("/docker_registry_debian_8.properties")

    DockerRegistryLinuxProperties() {
        super(DockerRegistryLinuxProperties.class, RESOURCE)
    }
}
