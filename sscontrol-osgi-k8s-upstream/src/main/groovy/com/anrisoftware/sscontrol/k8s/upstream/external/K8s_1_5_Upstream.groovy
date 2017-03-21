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
package com.anrisoftware.sscontrol.k8s.upstream.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>K8s-Node</i> 1.5 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8s_1_5_Upstream extends ScriptBase {

    def installKubernetes() {
        log.info 'Installs k8s-master.'
        copy src: archive, hash: archiveHash, dest: binDir, direct: true, privileged: true, timeout: timeoutLong call()
        shell privileged: true, "chmod o+x '$binDir/kubectl'" call()
    }

    URI getArchive() {
        properties.getURIProperty('kubectl_archive', defaultProperties)
    }

    String getArchiveHash() {
        properties.getProperty('kubectl_archive_hash', defaultProperties)
    }

    @Override
    def getLog() {
        log
    }
}
