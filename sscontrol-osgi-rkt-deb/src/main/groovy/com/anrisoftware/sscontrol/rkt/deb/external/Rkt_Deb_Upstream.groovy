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
package com.anrisoftware.sscontrol.rkt.deb.external

import static org.apache.commons.io.FilenameUtils.getBaseName

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Installs rkt from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Rkt_Deb_Upstream extends ScriptBase {

    def installRkt() {
        log.info 'Installs rkt.'
        copy src: archive, sig: archiveSig, server: archiveServer, key: archiveKey, dest: "/tmp", direct: true, timeout: timeoutLong call()
        def archiveFile = FilenameUtils.getName(archive.toString())
        def archiveName = getBaseName(getBaseName(archive.toString()))
        shell timeout: timeoutMiddle, """\
cd /tmp
sudo dpkg -i "$archiveFile"
""" call()
    }

    @Override
    def getLog() {
        log
    }
}