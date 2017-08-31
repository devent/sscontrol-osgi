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
package com.anrisoftware.sscontrol.icinga.script.icinga2.external

import javax.inject.Inject

import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.icinga.service.external.Config
import com.anrisoftware.sscontrol.icinga.service.external.Feature
import com.anrisoftware.sscontrol.icinga.service.external.Icinga
import com.anrisoftware.sscontrol.icinga.service.external.Plugin
import com.anrisoftware.sscontrol.types.host.external.HostServiceScriptService

import groovy.util.logging.Slf4j

/**
 * Configures Icinga 2 service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class AbstractIcinga2 extends ScriptBase {

    @Inject
    void loadTemplates(TemplatesFactory factory) {
    }

    def setupDefaults() {
        Icinga service = service
    }

    def configureFeatures() {
        log.info "Configure features for {}.", service
        Icinga service = service
        service.features.each { Feature feature ->
            log.debug "Configure feature {}.", feature
            def file = getFeatureFile feature
            copyString privileged: true, str: feature.script, dest: file
        }
    }

    def configureConf() {
        log.info "Configure configs for {}.", service
        Icinga service = service
        service.configs.each { Config config ->
            log.debug "Configure config {}.", config
            def file = getConfFile config
            copyString privileged: true, str: config.script, dest: file
        }
        shell privileged: true, """
cd $confDir
chown root.nagios *.conf
chmod g=r,o-r *.conf
""" call()
    }

    List<Map> installPlugins() {
        log.info "Create plugins for {}.", service
        Icinga service = service
        service.plugins.inject([]) { List result, Plugin plugin ->
            def pluginService = pluginServices[plugin.name]
            if (!pluginService) {
                pluginService = pluginServices['generic']
            }
            def p = pluginService.create(scriptsRepository, service, target, threads, scriptEnv)
            p.setupDefaults plugin
            p.installPlugin plugin
            p.configurePlugin plugin
            result << [plugin: plugin, script: p]
        }
    }

    def enablePlugins(List<Map> plugins) {
        log.info "Enable plugins for {}.", service
        plugins.each { Map map ->
            def plugin = map.plugin
            def script = map.script
            script.enablePlugin plugin
        }
    }

    abstract Map<String, HostServiceScriptService> getPluginServices()

    File getFeaturesAvailableDir() {
        getFileProperty "features_available_dir", base, defaultProperties
    }

    File getFeatureFile(Feature feature) {
        def name = feature.name
        new File("${name}.conf", featuresAvailableDir)
    }

    File getConfDir() {
        getFileProperty "conf_dir", base, defaultProperties
    }

    File getConfFile(Config config) {
        def name = config.name
        new File("${name}.conf", confDir)
    }

    @Override
    def getLog() {
        log
    }
}
