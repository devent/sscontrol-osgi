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
package com.anrisoftware.sscontrol.icinga.script.debian.internal.debian_9.icinga_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Installs Icinga 2 from the upstream repository for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class IcingaUpstreamDebian extends ScriptBase {

    @Inject
    IcingaDebianProperties debianPropertiesProvider

    DebianUtils debian

    SystemdUtils systemd

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setSystemdUtilsFactory(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    @Override
    Object run() {
        installIcinga()
        installNagiosPlugins()
    }

    def installIcinga() {
        def packages = [
            [name: icingaPackage, version: icingaVersion],
        ]
        if (debian.checkPackagesVersion(packages)) {
            return
        }
        debian.addPackagesRepository name: "icinga-${distributionName}"
        debian.installPackages packages: packages, update: true
    }

    def installNagiosPlugins() {
        def packages = [
            [name: "nagios-plugins", version: nagiosPluginsVersion],
        ]
        debian.installPackages packages: packages
    }

    def stopServices() {
        systemd.stopService()
    }

    def startServices() {
        systemd.startServices()
    }

    def enableServices() {
        systemd.enableServices()
    }

    String getIcingaPackage() {
        properties.getProperty "icinga_package", defaultProperties
    }

    String getIcingaVersion() {
        properties.getProperty "icinga_version", defaultProperties
    }

    String getNagiosPluginsVersion() {
        properties.getProperty "nagios_plugins_version", defaultProperties
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
