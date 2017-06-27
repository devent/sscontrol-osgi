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
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory

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

    CentosUtils centos

    @Override
    Object run() {
        removePostfix()
        installZimbra()
    }

    @Inject
    void setCentosFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create(this)
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
