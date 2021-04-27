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
package com.anrisoftware.sscontrol.collectd.script.debian.internal.debian_10

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.collectd.script.debian.external.Collectd_Debian
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Collectd for Debian 10.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Collectd_Debian_10 extends Collectd_Debian {

    static final String SYSTEM_VERSION = "10";

    static final String SYSTEM_NAME = "debian";

    @Inject
    Collectd_Debian_10_Properties propertiesProvider

    Collectd_5_8_Debian_10 collectd

    SystemdUtils systemd

    @Inject
    void setCollectd_5_7_Centos_7_Factory(Collectd_5_8_Debian_10_Factory factory) {
        this.collectd = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create(this)
    }

    @Override
    def run() {
        systemd.stopServices()
        installPackages()
        collectd.deployConfiguration()
        systemd.startServices()
        systemd.enableServices()
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
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
