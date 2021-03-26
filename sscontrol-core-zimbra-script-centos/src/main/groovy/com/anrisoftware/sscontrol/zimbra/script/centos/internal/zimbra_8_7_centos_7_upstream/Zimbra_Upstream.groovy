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

import static org.apache.commons.io.FilenameUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory
import com.anrisoftware.sscontrol.utils.st.durationrenderer.external.DurationAttributeFormat

import groovy.util.logging.Slf4j

/**
 * Installs Zimbra from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Zimbra_Upstream extends ScriptBase {

    @Inject
    Zimbra_Properties propertiesProvider

    TemplateResource zimbraInstallExpectTemplate

    TemplateResource zimbraInstallCheckTemplate

    TemplateResource zimbraUpdateExpectTemplate

    CentosUtils centos

    @Override
    Object run() {
        removePostfix()
        def version = installedZimbraVersion
        if (!version) {
            installZimbra()
        } else {
            if (autoUpdateZimbra) {
                updateZimbra()
            }
        }
    }

    @Inject
    void setCentosFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create(this)
    }

    @Inject
    void loadTemplates(TemplatesFactory templatesFactory, DurationAttributeFormat durationAttributeFormat) {
        def attr = [renderers: [durationAttributeFormat]]
        def templates = templatesFactory.create('Zimbra_Upstream_Templates', attr)
        this.zimbraInstallExpectTemplate = templates.getResource('zimbra_install_expect')
        this.zimbraInstallCheckTemplate = templates.getResource('zimbra_install_check')
        this.zimbraUpdateExpectTemplate = templates.getResource('zimbra_update_expect')
    }

    def removePostfix() {
        if (!centos.checkPackage(package: 'postfix')) {
            return
        }
        log.info 'Removes postfix from system.'
        shell privileged: true, """\
yum remove -y postfix
""" call()
    }

    def getInstalledZimbraVersion() {
        try {
            def ret = shell privileged: true, outString: true,
            resource: zimbraInstallCheckTemplate, name: 'returnZimbraVersion' call()
            return ret.out
        } catch (e) {
            return false
        }
    }

    def installZimbra() {
        log.info 'Installs Zimbra.'
        def dir = downloadZimbra()
        try {
            startInstallation dir
        } finally {
            shell "rm -rf $dir" call()
        }
    }

    File downloadZimbra() {
        log.info 'Downloads Zimbra.'
        def dir = '/tmp'
        copy src: archive, hash: archiveHash, dest: dir, direct: true, timeout: timeoutLong call()
        def archiveFile = getName(archive.toString())
        def archiveName = getBaseName(archive.toString())
        shell timeout: timeoutMiddle, """\
cd "$dir"
tar xf $archiveFile
""" call()
        return new File(dir, archiveName)
    }

    def startInstallation(File dir) {
        log.info 'Start installation of Zimbra.'
        shell privileged: true, timeout: timeoutLong,
        vars: [zimbraDir: dir],
        resource: zimbraInstallExpectTemplate, name: 'installZimbra' call()
    }

    def updateZimbra() {
        log.info 'Start update of Zimbra.'
        def dir = downloadZimbra()
        try {
            startUpdate dir
        } finally {
            shell "rm -rf $dir" call()
        }
    }

    def startUpdate(File dir) {
        shell privileged: true, timeout: timeoutLong,
        vars: [zimbraDir: dir],
        resource: zimbraUpdateExpectTemplate, name: 'updateZimbra' call()
    }

    boolean getAutoUpdateZimbra() {
        getScriptBooleanProperty 'auto_update_zimbra'
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
