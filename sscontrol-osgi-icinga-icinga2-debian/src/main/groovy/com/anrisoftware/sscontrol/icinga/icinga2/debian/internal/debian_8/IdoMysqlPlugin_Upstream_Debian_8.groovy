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
package com.anrisoftware.sscontrol.icinga.icinga2.debian.internal.debian_8

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.icinga.icinga2.debian.external.debian_8.SetupDatabaseException
import com.anrisoftware.sscontrol.icinga.service.external.Plugin

import groovy.util.logging.Slf4j

/**
 * Installs ido-mysql plugin from the upstream repository for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class IdoMysqlPlugin_Upstream_Debian_8 extends ScriptBase {

    @Inject
    Icinga_2_Debian_8_Properties debianPropertiesProvider

    @Override
    Object run() {
    }

    def setupDefaults(Plugin plugin) {
        log.info "Setup defaults for {}", plugin
    }

    def installPlugin(Plugin plugin) {
        log.info "Install plugin {}.", plugin
        def packages = [
            [package: pluginPackage, version: pluginVersion],
        ]
        if (checkAptPackagesVersion(packages)) {
            return
        }
        addAptPackagesRepository([name: "icinga-${distributionName}"])
        installAptPackages(packages.inject([]) { result, map ->
            result << "${map.package}=${map.version}"
        })
    }

    def configurePlugin(Plugin plugin) {
        log.info "Configure plugin {}.", plugin
        def ret = shell checkExitCodes: false, errString: true, vars: [plugin: plugin], st: """
mysql -u<vars.plugin.database.user> -p<vars.plugin.database.password> <vars.plugin.database.database> \\< /usr/share/icinga2-ido-mysql/schema/mysql.sql
""" call()
        if (ret.exitValue != 0) {
            if (ret.err =~ /ERROR 1060/) {
                log.debug "Already configured plugin {}.", plugin
                return
            }
            throw new SetupDatabaseException(plugin, ret)
        }
    }

    def enablePlugin(Plugin plugin) {
        log.info "Enable plugin {}.", plugin
        shell privileged: true, """
icinga2 feature enable ido-mysql
""" call()
    }

    String getPluginPackage() {
        properties.getProperty "ido_mysql_plugin_package", defaultProperties
    }

    String getPluginVersion() {
        properties.getProperty "ido_mysql_plugin_version", defaultProperties
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
