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

import static org.apache.commons.io.FilenameUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory
import com.anrisoftware.sscontrol.utils.st.durationrenderer.external.DurationAttributeFormat
import com.anrisoftware.sscontrol.zimbra.service.external.Zimbra

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

    TemplateResource certbotZimbraExpect

    TemplateResource stopDockerCmd

    @Inject
    void setCentosFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create(this)
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, DurationAttributeFormat durationAttributeFormat) {
        def attr = [renderers: [durationAttributeFormat]]
        def templates = templatesFactory.create('Zimbra_LetsEncrypt_Docker_Templates', attr)
        this.certbotDockerTemplate = templates.getResource('certbot_docker_template')
        this.certbotZimbraExpect = templates.getResource('certbot_zimbra_expect')
        this.stopDockerCmd = templates.getResource('stop_docker_cmd')
    }

    @Override
    def run() {
        if(!checkDomain()) {
            return
        }
        startDocker()
        def certbotZimbraDir = downloadCertbotzimbra()
        try {
            setupCert certbotZimbraDir
        } finally {
            shell "rm -rf ${certbotZimbraDir}" call()
            stopDocker()
        }
    }

    boolean checkDomain() {
        Zimbra service = this.service
        if (!service.domain.email) {
            return false
        }
        return true
    }

    def startDocker() {
        log.info 'Start docker for certbot.'
        centos.installPackages(packages: ['docker', 'unzip'])
        startSystemdService 'docker'
    }

    def stopDocker() {
        def ret = shell outString: true, privileged: true, resource: stopDockerCmd, name: "stopDockerCmd" call()
        int count = Integer.valueOf ret.out
        if (count == 1) {
            stopSystemdService 'docker'
        }
    }

    File downloadCertbotzimbra() {
        log.info 'Download certbot-zimbra.'
        shell privileged: true, resource: certbotDockerTemplate, name: 'createCertbot' call()
        def archive = certbotZimbraArchive
        copy src: archive, dest: '/tmp', direct: true, timeout: timeoutMiddle call()
        def archiveFile = getName(archive.toString())
        def archiveName = getBaseName(archive.toString())
        shell """
cd /tmp
if [ -d 'certbot-zimbra-${archiveName}' ]; then rm -rf "certbot-zimbra-${archiveName}"; fi
unzip <if(parent.commandsQuiet)>-q<endif> $archiveFile
mv "certbot-zimbra-${archiveName}" "certbot-zimbra"
            """ call()
        return new File("/tmp/certbot-zimbra")
    }

    def setupCert(File certbotZimbraDir) {
        log.info 'Setup certificate from certbot-zimbra: {}', certbotZimbraDir
        shell privileged: true, timeout: timeoutLong,
        vars: [certbotDir: certbotZimbraDir],
        resource: certbotZimbraExpect, name: 'certbotZimbraExpect' call()
    }

    URI getCertbotZimbraArchive() {
        properties.getURIProperty 'certbot_zimbra_archive', defaultProperties
    }

    boolean getForceRenewCertbotCertificate() {
        properties.getBooleanProperty 'force_renew_certbot_certificate', defaultProperties
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
