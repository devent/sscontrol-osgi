package com.anrisoftware.sscontrol.icinga.icinga2.debian.internal.debian_8

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.icinga.service.external.Plugin

import groovy.util.logging.Slf4j

/**
 * Installs the plugin from the upstream repository for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractPlugin_Upstream_Debian_8 extends ScriptBase {

    @Inject
    Icinga_2_Debian_8_Properties debianPropertiesProvider

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
