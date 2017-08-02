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
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_17

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_8_UtilsFactory

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

    DebianUtils debian

    @Inject
    void setDebianUtilsFactory(Debian_8_UtilsFactory factory) {
        this.debian = factory.create(this)
    }

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
        debian.addBackportsRepository()
        debian.installBackportsPackages([
            "$kernelPackage=$kernelVersion"
        ])
    }

    def installDocker() {
        if (debian.checkPackage(package: dockerPackage, version: dockerVersion)) {
            return
        }
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

    String getKernelPackage() {
        properties.getProperty 'kernel_package', defaultProperties
    }

    String getKernelVersion() {
        properties.getProperty 'kernel_version', defaultProperties
    }

    String getKernelFullVersion() {
        properties.getProperty 'kernel_full_version', defaultProperties
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