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
package com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal

import static com.anrisoftware.sscontrol.flanneldocker.flanneldocker_0_7_debian.internal.FlannelDocker_0_7_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.flanneldocker.upstream.external.FlannelDocker_0_7_Upstream
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Configures the Flannel-Docker 0.7 service from the upstream sources
 * for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class FlannelDocker_0_7_Upstream_Debian_8 extends FlannelDocker_0_7_Upstream {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface FlannelDocker_0_7_Upstream_Debian_8_Factory extends HostServiceScriptService {
    }

    @Inject
    FlannelDocker_0_7_Debian_8_Properties debianPropertiesProvider

    @Override
    Object run() {
        installKubernetes()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
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
