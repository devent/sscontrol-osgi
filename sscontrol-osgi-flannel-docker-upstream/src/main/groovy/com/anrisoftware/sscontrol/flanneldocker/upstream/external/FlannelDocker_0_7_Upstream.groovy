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
package com.anrisoftware.sscontrol.flanneldocker.upstream.external

import static org.apache.commons.io.FilenameUtils.getBaseName

import javax.inject.Inject

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.external.TemplateResource
import com.anrisoftware.resources.templates.external.TemplatesFactory
import com.anrisoftware.sscontrol.flanneldocker.external.FlannelDocker
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Flannel-Docker</i> 0.7 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class FlannelDocker_0_7_Upstream extends ScriptBase {

    TemplateResource installCmdResource

    @Inject
    def setTemplates(TemplatesFactory factory, NetworkRenderer networkRenderer) {
        def attr = [renderers: [networkRenderer]]
        def t = factory.create 'FlannelDocker_0_7_Upstream_Templates', attr
        this.installCmdResource = t.getResource 'install_cmd'
    }

    def installFlannel() {
        log.info 'Installs Flannel-Docker.'
        FlannelDocker service = this.service
        copy src: archive, hash: archiveHash, dest: "/tmp", direct: true call()
        shell resource: installCmdResource, name: 'installCmd' call()
    }

    URI getArchive() {
        properties.getURIProperty 'flannel_archive', defaultProperties
    }

    String getArchiveFile() {
        FilenameUtils.getName(archive.toString())
    }

    String getArchiveName() {
        getBaseName(getBaseName(archive.toString()))
    }

    String getArchiveHash() {
        properties.getProperty 'flannel_archive_hash', defaultProperties
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    File getLibexecDir() {
        properties.getFileProperty "libexec_dir", base, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
