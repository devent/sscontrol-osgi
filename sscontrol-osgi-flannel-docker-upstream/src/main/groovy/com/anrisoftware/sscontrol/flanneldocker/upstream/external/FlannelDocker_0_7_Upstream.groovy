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

import org.apache.commons.io.FilenameUtils

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

    def installKubernetes() {
        log.info 'Installs Flannel-Docker.'
        copy src: archive, hash: archiveHash, dest: "/tmp", direct: true call()
        def archiveFile = FilenameUtils.getName(archive.toString())
        def archiveName = getBaseName(getBaseName(archive.toString()))
        def bindir = binDir
        def libexecdir = libexecDir
        shell """\
cd /tmp
tar xf "$archiveFile"
sudo mkdir -p '$bindir'
sudo mv flanneld '$bindir'
sudo mkdir -p '$libexecdir'
sudo mv mk-docker-opts.sh '$libexecdir'
""" call()
    }

    URI getArchive() {
        properties.getURIProperty 'etcd_archive', defaultProperties
    }

    String getArchiveHash() {
        properties.getProperty 'etcd_archive_hash', defaultProperties
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
