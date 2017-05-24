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
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
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
class IdoMysqlPlugin_Upstream_Debian_8 extends AbstractPlugin_Upstream_Debian_8 {

    @Inject
    Icinga_2_Debian_8_Properties debianPropertiesProvider

    TemplateResource mysqlTemplate

    @Inject
    void loadTemplates(TemplatesFactory factory) {
        def templates = factory.create('IdoMysqlPlugin_Upstream_Debian_8_Templates')
        this.mysqlTemplate = templates.getResource('mysql_cmd')
    }

    def configurePlugin(Plugin plugin) {
        log.info "Configure plugin {}.", plugin
        def ret = shell checkExitCodes: false, errString: true, vars: [plugin: plugin], resource: mysqlTemplate, name: 'setupMysql' call()
        if (ret.exitValue != 0) {
            if (ret.err =~ /ERROR 1060/) {
                log.debug "Already configured plugin {}.", plugin
                return
            }
            throw new SetupDatabaseException(plugin, ret)
        }
    }

    String getPluginPackage() {
        properties.getProperty "ido_mysql_plugin_package", defaultProperties
    }

    String getPluginVersion() {
        properties.getProperty "ido_mysql_plugin_version", defaultProperties
    }

    File getIdoMysqlSchemaScriptFile() {
        getFileProperty "ido_mysql_schema_script_file", base, defaultProperties
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
