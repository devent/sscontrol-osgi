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
package com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal

import static com.anrisoftware.sscontrol.sshd.sshd_6_debian.internal.Sshd_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.sshd.external.Sshd
import com.anrisoftware.sscontrol.sshd.sshd_6_debian.external.Sshd_6_Debian

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> 6 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Sshd_Debian_8 extends Sshd_6_Debian {

    @Inject
    Sshd_Debian_8_Properties debianPropertiesProvider

    @Override
    def run() {
        setupDefaults()
        installPackages()
        configureService()
        restartService()
    }

    def setupDefaults() {
        Sshd service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug level: defaultLogLevel
        }
    }

    def getDefaultLogLevel() {
        defaultProperties.getNumberProperty('default_log_level').intValue()
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    Sshd getService() {
        super.getService();
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
