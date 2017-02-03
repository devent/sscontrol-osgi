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
package com.anrisoftware.sscontrol.k8smaster.upstream.external

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the <i>K8s-Master</i> 1.5 service from the upstream sources.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class K8sMaster_1_5_Upstream extends ScriptBase {

    def installKubernetes() {
        log.info 'Installs k8s-master.'
        copy src: archive, hash: archiveHash, dest: "/tmp", direct: true, timeout: timeoutLong call()
        shell timeout: timeoutVeryLong, """\
cd /tmp
tar xf `basename $archive`
cd kubernetes
printf "y\\n" | cluster/get-kube-binaries.sh
cd server
tar xf kubernetes-server-linux-amd64.tar.gz
mkdir -p '$binDir'
cd kubernetes/server/bin
sudo find . -executable -type f -exec cp '{}' '$binDir' \\;
sudo chmod o+rx '$binDir'/*
""" call()
    }

    URI getArchive() {
        properties.getURIProperty('kubernetes_archive', defaultProperties)
    }

    String getArchiveHash() {
        properties.getProperty('kubernetes_archive_hash', defaultProperties)
    }

    File getBinDir() {
        properties.getFileProperty "bin_dir", base, defaultProperties
    }

    @Override
    def getLog() {
        log
    }
}
