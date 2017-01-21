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
package com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal

import static com.anrisoftware.sscontrol.k8smaster.k8smaster_1_5_debian.internal.K8sMaster_1_5_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.k8smaster.systemd.external.K8sMaster_1_5_Systemd
import com.anrisoftware.sscontrol.types.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Configures the K8s-Master 1.5 service using Systemd and Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class K8sMaster_1_5_Systemd_Debian_8 extends K8sMaster_1_5_Systemd {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    interface K8sMaster_1_5_Systemd_Debian_8_Factory extends HostServiceScriptService {
    }

    @Inject
    K8sMaster_1_5_Debian_8_Properties debianPropertiesProvider

    @Override
    Object run() {
        restartServices()
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