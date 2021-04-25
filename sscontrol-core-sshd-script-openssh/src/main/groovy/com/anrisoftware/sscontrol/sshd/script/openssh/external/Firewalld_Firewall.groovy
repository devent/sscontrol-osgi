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
package com.anrisoftware.sscontrol.sshd.script.openssh.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.sshd.service.external.Sshd
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils

import groovy.util.logging.Slf4j

/**
 * Configures firewalld firewall for the Sshd service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Firewalld_Firewall extends Firewall {

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
        Sshd service = service
        if (!active) {
            log.debug 'No firewalld available.'
            return
        }
        updateFirewall()
    }

    def updateFirewall() {
        Sshd service = service
        if (service.binding.port == 22) {
            shell privileged: true, "firewall-cmd --zone=public --add-service=ssh" call()
        } else {
            shell privileged: true, "firewall-cmd --zone=public --add-port=${service.binding.port}/tcp" call()
        }
    }

    boolean isActive() {
        def ret = shell privileged: true, outString: true, exitCodes: [0, 1] as int[], "which firewall-cmd>/dev/null && firewall-cmd --state" call()
        return ret.exitValue == 0 && (ret.out =~ /running.*/)
    }

    String getFirewallZone() {
        getScriptProperty 'firewall_zone'
    }
}
