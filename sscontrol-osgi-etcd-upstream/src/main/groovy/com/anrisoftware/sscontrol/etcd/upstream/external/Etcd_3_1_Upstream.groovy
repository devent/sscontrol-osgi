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
package com.anrisoftware.sscontrol.etcd.upstream.external

import static org.apache.commons.io.FilenameUtils.getBaseName

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Etcd</i> 3.1 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_1_Upstream extends ScriptBase {

    def installEtcd() {
        log.info 'Installs etcd.'
        copy src: archive, sig: archiveSig, server: archiveServer, key: archiveKey, dest: "/tmp", direct: true, timeout: timeoutLong call()
        def archiveFile = FilenameUtils.getName(archive.toString())
        def archiveName = getBaseName(getBaseName(archive.toString()))
        shell timeout: timeoutMiddle, """\
cd /tmp
tar xf "$archiveFile"
cd "$archiveName"
sudo find . -executable -type f -exec cp '{}' '$binDir' \\;
sudo chmod o+rx '$binDir'/*
""" call()
    }

    @Override
    def getLog() {
        log
    }
}
