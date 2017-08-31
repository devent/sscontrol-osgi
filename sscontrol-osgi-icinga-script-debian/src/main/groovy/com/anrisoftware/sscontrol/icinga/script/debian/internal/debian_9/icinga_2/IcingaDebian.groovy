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
import com.anrisoftware.sscontrol.icinga.script.icinga2.external.AbstractIcinga2
import com.anrisoftware.sscontrol.icinga.service.external.Icinga
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Configures the Icinga 2 service for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class IcingaDebian extends AbstractIcinga2 {

    @Inject
    IcingaDebianProperties debianPropertiesProvider

    @Inject
    Map<String, PluginHostServiceScriptService> pluginServices

    IcingaUpstreamDebian upstream

    @Inject
    void setUpstreamFactory(IcingaUpstreamDebianFactory factory) {
        this.upstream = factory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Override
    def run() {
        setupDefaults()
        upstream.stopServices()
        upstream.run()
        def plugins = installPlugins()
        configureFeatures()
        configureConf()
        enablePlugins(plugins)
        upstream.startServices()
    }

    def setupDefaults() {
        super.setupDefaults()
        log.info "Setup defaults for {}.", service
        Icinga service = service
    }

    @Override
    Map<String, HostServiceScriptService> getPluginServices() {
        return pluginServices
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
