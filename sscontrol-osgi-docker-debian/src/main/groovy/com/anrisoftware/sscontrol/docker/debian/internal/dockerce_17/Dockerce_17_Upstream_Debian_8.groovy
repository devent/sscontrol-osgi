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
package com.anrisoftware.sscontrol.docker.debian.internal.dockerce_17

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Installs Docker CE 17 from the upstream repository for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Dockerce_17_Upstream_Debian_8 extends ScriptBase {

    @Inject
    Dockerce_17_Debian_8_Properties debianPropertiesProvider

    @Override
    Object run() {
        if (upgradeKernel) {
            installKernel()
        }
        installDocker()
    }

    def installKernel() {
        if (check("uname -a | grep '$kernelFullVersion'")) {
            return
        }
        shell privileged: true, timeout: timeoutLong, """
echo "deb ${kernelRepository} ${distributionName}-backports main" > $backportsListFile
apt-get update && apt-get -y install -t ${distributionName}-backports $kernelPackage=$kernelVersion
""" with { sudoEnv "DEBIAN_FRONTEND=noninteractive" } call()
    }

    def installDocker() {
        shell """
curl -fsSL $dockerRepositoryKey | sudo apt-key add -
sudo bash -c 'echo "deb [arch=amd64] $dockerRepository $distributionName stable" > $dockerListFile'
""" call()
        shell privileged: true, timeout: timeoutLong, "apt-get update && apt-get -y install $dockerPackage=$dockerVersion" with { //
            sudoEnv "DEBIAN_FRONTEND=noninteractive" } call()
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

    boolean getUpgradeKernel() {
        properties.getBooleanProperty 'upgrade_kernel', defaultProperties
    }

    String getKernelRepository() {
        properties.getProperty 'kernel_repository', defaultProperties
    }

    String getKernelPackage() {
        properties.getProperty 'kernel_package', defaultProperties
    }

    String getKernelVersion() {
        properties.getProperty 'kernel_version', defaultProperties
    }

    String getKernelFullVersion() {
        properties.getProperty 'kernel_full_version', defaultProperties
    }

    File getBackportsListFile() {
        getFileProperty 'backports_list_file'
    }

    File getDockerListFile() {
        getFileProperty 'docker_list_file'
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
