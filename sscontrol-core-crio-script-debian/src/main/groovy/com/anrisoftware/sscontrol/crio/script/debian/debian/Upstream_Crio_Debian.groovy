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
package com.anrisoftware.sscontrol.crio.script.debian.debian

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 * CRI-O from upstream.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Upstream_Crio_Debian extends ScriptBase {

    def installCrio() {
        debian.checkPackages() ? { }() : {
            debian.addBackportsRepository()
            debian.addPackagesRepositoryAlternative(key: libcontainersRepositoryKey, url: libcontainersPepositoryUrl, file: libcontainersRepositoryListFile)
            debian.addPackagesRepositoryAlternative(key: crioRepositoryKey, url: crioPepositoryUrl, file: crioRepositoryListFile)
            debian.installPackages()
        }()
    }

    abstract DebianUtils getDebian()

    /**
     * For example <code>/etc/apt/sources.list.d/libcontainers.list</code>
     * <ul>
     * <li><code>libcontainers_repository_list_file</code>
     * </ul>
     */
    File getLibcontainersRepositoryListFile() {
        getScriptFileProperty 'libcontainers_repository_list_file'
    }

    /**
     * For example <code>https://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable/Debian_Testing/</code>
     * <ul>
     * <li><code>libcontainers_pepository_url</code>
     * </ul>
     */
    String getLibcontainersPepositoryUrl() {
        getScriptProperty 'libcontainers_pepository_url'
    }

    /**
     * For example <code>https://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable/Debian_Testing/Release.key</code>
     * <ul>
     * <li><code>libcontainers_repository_key</code>
     * </ul>
     */
    String getLibcontainersRepositoryKey() {
        getScriptProperty 'libcontainers_repository_key'
    }

    /**
     * For example <code>/etc/apt/sources.list.d/crio.list</code>
     * <ul>
     * <li><code>crio_repository_list_file</code>
     * </ul>
     */
    File getCrioRepositoryListFile() {
        getScriptFileProperty 'crio_repository_list_file'
    }

    /**
     * For example <code>http://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable:/cri-o:/1.20/Debian_Testing/</code>
     * <ul>
     * <li><code>crio_pepository_url</code>
     * </ul>
     */
    String getCrioPepositoryUrl() {
        getScriptProperty 'crio_pepository_url'
    }

    /**
     * For example <code>https://download.opensuse.org/repositories/devel:kubic:libcontainers:stable:cri-o:1.20/Debian_Testing/Release.key</code>
     * <ul>
     * <li><code>crio_repository_key</code>
     * </ul>
     */
    String getCrioRepositoryKey() {
        getScriptProperty 'crio_repository_key'
    }

    @Override
    def getLog() {
        log
    }
}
