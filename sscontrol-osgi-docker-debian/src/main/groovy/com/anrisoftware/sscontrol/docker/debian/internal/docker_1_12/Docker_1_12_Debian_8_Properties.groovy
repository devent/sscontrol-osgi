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
package com.anrisoftware.sscontrol.docker.debian.internal.docker_1_12;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Docker 1.12 Debian 8</i> properties provider from
 * {@code "/docker_1_12_debian_8.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Docker_1_12_Debian_8_Properties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Docker_1_12_Debian_8_Properties.class.getResource("/docker_1_12_debian_8.properties");

    Docker_1_12_Debian_8_Properties() {
        super(Docker_1_12_Debian_8_Properties.class, RESOURCE);
    }
}
