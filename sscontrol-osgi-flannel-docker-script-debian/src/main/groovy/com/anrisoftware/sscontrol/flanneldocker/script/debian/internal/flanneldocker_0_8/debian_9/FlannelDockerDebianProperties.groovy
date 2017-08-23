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
package com.anrisoftware.sscontrol.flanneldocker.script.debian.internal.flanneldocker_0_8.debian_9;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider

/**
 * <i>Flannel-Docker 0.8 Debian 9</i> properties provider from
 * {@code "/flanneldocker_0_8_debian_9.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FlannelDockerDebianProperties extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = FlannelDockerDebianProperties.class.getResource("/flanneldocker_0_8_debian_9.properties");

    FlannelDockerDebianProperties() {
        super(FlannelDockerDebianProperties.class, RESOURCE);
    }
}
