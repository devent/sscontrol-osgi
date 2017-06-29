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
 * Uses docker to install certbot to create LetsEncrypt certificates for Zimbra.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Zimbra_LetsEncrypt_Docker extends ScriptBase {

    @Inject
    Zimbra_Properties propertiesProvider

    CentosUtils centos

    TemplateResource certbotDockerTemplate

    @Inject
    void setCentosFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create(this)
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create('Zimbra_LetsEncrypt_Docker_Templates')
        this.certbotDockerTemplate = templates.getResource('certbot_docker_template')
    }

    @Override
    def run() {
        centos.checkPackage(package: 'docker') ?: centos.installPackages(packages: 'docker')
        centos.checkPackage(package: 'unzip') ?: centos.installPackages(packages: 'unzip')
        startSystemdService 'docker'
        shell privileged: true, resource: certbotDockerTemplate, name: 'createCertbot' call()
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
