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
package com.anrisoftware.sscontrol.zimbra.script.centos.internal.zimbra_8_7_centos_7_upstream

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * rkt 1.26 for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Zimbra_Script extends ScriptBase {

    @Inject
    Zimbra_Properties propertiesProvider

    @Inject
    Zimbra_Upstream_Factory upstreamFactory

    @Inject
    ZimbraLetsEncryptFactory letsEncryptFactory

    CentosUtils centos

    TemplateResource zimbraFirewallTemplate

    @Inject
    void setCentosFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create(this)
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Zimbra_Script_Templates')
        this.zimbraFirewallTemplate = templates.getResource('zimbra_firewall_template')
    }

    @Override
    def run() {
        centos.checkPackages() ? false : centos.installPackages()
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        configureFirewall()
        if (useLetsEncrypt) {
            letsEncryptFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        }
    }

    def configureFirewall() {
        log.info 'Start installation of Zimbra.'
        shell privileged: true,
        resource: zimbraFirewallTemplate, name: 'configureFirewall' call()
    }

    boolean getAllowHttp() {
        getScriptBooleanProperty 'allow_http'
    }

    boolean getAllowHttps() {
        getScriptBooleanProperty 'allow_https'
    }

    boolean getAllowSmtp() {
        getScriptBooleanProperty 'allow_smtp'
    }

    boolean getAllowImap() {
        getScriptBooleanProperty 'allow_imap'
    }

    boolean getAllowImaps() {
        getScriptBooleanProperty 'allow_imaps'
    }

    boolean getAllowAdminConsole() {
        getScriptBooleanProperty 'allow_admin_console'
    }

    boolean getUseLetsEncrypt() {
        getScriptBooleanProperty 'use_letsencrypt'
    }

    File getZimbraFirewallServiceFile() {
        getScriptFileProperty 'zimbra_firewall_service_file'
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
