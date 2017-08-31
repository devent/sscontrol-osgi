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
import com.anrisoftware.sscontrol.icinga.service.external.Plugin
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_9_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Installs the plugin from the upstream repository for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractPluginUpstreamDebian extends ScriptBase {

    @Inject
    IcingaDebianProperties debianPropertiesProvider

    DebianUtils debian

    @Inject
    void setDebianUtilsFactory(Debian_9_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Override
    Object run() {
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    def setupDefaults(Plugin plugin) {
        log.info "Setup defaults for {}", plugin
    }

    def installPlugin(Plugin plugin) {
        log.info "Install plugin {}.", plugin
        def packages = [
            [name: pluginPackage, version: pluginVersion],
        ]
        if (debian.checkPackagesVersion(packages)) {
            return
        }
        debian.addPackagesRepository name: "icinga-${distributionName}"
        debian.installPackages packages: packages, update: true
    }

    def configurePlugin(Plugin plugin) {
        log.info "Configure plugin {}.", plugin
    }

    def enablePlugin(Plugin plugin) {
        log.info "Enable plugin {}.", plugin
        shell privileged: true, """
icinga2 feature enable ${plugin.name}
""" call()
    }

    String getPluginPackage() {
    }

    String getPluginVersion() {
    }
}
