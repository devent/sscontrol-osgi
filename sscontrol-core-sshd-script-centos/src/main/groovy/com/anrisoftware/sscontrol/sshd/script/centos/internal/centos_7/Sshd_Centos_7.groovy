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
package com.anrisoftware.sscontrol.sshd.script.centos.internal.centos_7

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.sshd.script.openssh.external.SshdSystemd
import com.anrisoftware.sscontrol.sshd.service.external.Sshd
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.centos.external.Centos_7_UtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> service for CentOS 7.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Sshd_Centos_7 extends SshdSystemd {

    @Inject
    Sshd_Centos_7_Properties centosProperties

    @Inject
    Firewalld_Firewall_Centos_7_Factory firewalldFactory

    CentosUtils centos

    @Inject
    void setCentosUtilsFactory(Centos_7_UtilsFactory factory) {
        this.centos = factory.create this
    }

    @Override
    def run() {
        setupDefaults()
        installPackages()
        setupSelinux()
        configureService()
        restartService()
        firewallScript.run()
    }

    def setupDefaults() {
        Sshd service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug level: defaultLogLevel
        }
        if (!service.binding.port) {
            service.bind port: defaultPort
        }
    }

    void installPackages() {
        centos.installPackages()
    }

    void setupSelinux() {
        selinuxActive ? { log.debug "SELinux activated."; configureSelinux() }(): { log.debug "SELinux not activated." }()
    }

    void configureSelinux() {
        centos.installPackages selinuxPackages
        selinuxSshPort ? { }() : { updateSelinuxPort() }()
    }

    def getDefaultPort() {
        getScriptNumberProperty 'default_port' intValue()
    }

    def getFirewalldFirewallScript() {
        firewalldFactory.create scriptsRepository, service, target, threads, scriptEnv
    }

    @Override
    ContextProperties getDefaultProperties() {
        centosProperties.get()
    }

    @Override
    def getLog() {
        log
    }
}
