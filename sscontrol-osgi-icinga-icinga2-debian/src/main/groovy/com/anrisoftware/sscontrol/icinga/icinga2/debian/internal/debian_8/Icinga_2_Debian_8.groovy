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
import com.anrisoftware.sscontrol.icinga.icinga2.script.external.Icinga_2
import com.anrisoftware.sscontrol.icinga.service.external.Icinga
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Configures the Icinga 2 service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Icinga_2_Debian_8 extends Icinga_2 {

    @Inject
    Icinga_2_Debian_8_Properties debianPropertiesProvider

    @Inject
    Map<String, PluginHostServiceScriptService> pluginServices

    Icinga_2_Upstream_Debian_8 upstream

    @Inject
    void setUpstreamFactory(Icinga_2_Upstream_Debian_8_Factory factory) {
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
