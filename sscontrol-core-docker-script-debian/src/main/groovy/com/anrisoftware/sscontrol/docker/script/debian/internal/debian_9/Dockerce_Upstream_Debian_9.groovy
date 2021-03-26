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
package com.anrisoftware.sscontrol.docker.script.debian.internal.debian_9

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 * Installs Docker CE from the upstream repository for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Dockerce_Upstream_Debian_9 extends ScriptBase {

    abstract DebianUtils getDebian()

    @Override
    Object run() {
        installDocker()
    }

    def installDocker() {
        if (debian.checkPackage(name: dockerPackage, version: dockerVersion)) {
            return
        }
        shell """
curl -fsSL $dockerRepositoryKey | sudo apt-key add -
sudo bash -c 'echo "deb [arch=amd64] $dockerRepository $distributionName stable" > $dockerListFile'
""" call()
        debian.installPackages(update: true, packages: [
            [name: dockerPackage, version: dockerVersion]
        ])
    }

    String getDockerRepository() {
        properties.getProperty 'docker_repository', defaultProperties
    }

    String getDockerRepositoryKey() {
        properties.getProperty 'docker_repository_key', defaultProperties
    }

    String getDockerPackage() {
        properties.getProperty 'docker_package', defaultProperties
    }

    String getDockerVersion() {
        properties.getProperty 'docker_version', defaultProperties
    }

    File getDockerListFile() {
        getFileProperty 'docker_list_file'
    }

    @Override
    def getLog() {
        log
    }
}
