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
package com.anrisoftware.sscontrol.docker.script.debian.internal.dockerce_18_debian_9

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.docker.script.debian.internal.debian.Dockerce_Systemd_Debian
import com.anrisoftware.sscontrol.docker.script.systemd.external.Dockerce_17_Systemd
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_10_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the Docker CE 18 service for Debian 9.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Dockerce_18_Debian_9 extends Dockerce_Systemd_Debian {

    @Inject
    Dockerce_18_Debian_9_Properties debianPropertiesProvider

    @Inject
    Dockerce_18_Upstream_Debian_9_Factory upstreamFactory

    DebianUtils debian

    Dockerce_17_Systemd dockerSystemd

    @Inject
    def setSystemdFactory(Dockerce_18_Systemd_Debian_9_Factory systemdFactory) {
        this.dockerSystemd = systemdFactory.create(scriptsRepository, service, target, threads, scriptEnv)
    }

    @Inject
    void setDebianUtilsFactory(Debian_10_UtilsFactory factory) {
        this.debian = factory.create(this)
    }

    @Override
    def run() {
        setupDefaults()
        dockerSystemd.stopServices()
        dockerSystemd.setupDefaults()
        dockerSystemd.setupDefaultLogDriver()
        dockerSystemd.createDirectories()
        dockerSystemd.createDockerdConfig()
        dockerSystemd.createRegistryMirrorConfig()
        dockerSystemd.deployMirrorCerts()
        debian.installPackages()
        upgradeKernel ? installKernel() : false
        upstreamFactory.create(scriptsRepository, service, target, threads, scriptEnv).run()
        dockerSystemd.createDaemonConfig()
        dockerSystemd.startServices()
        updateGrub()
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
