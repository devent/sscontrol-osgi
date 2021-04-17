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
package com.anrisoftware.sscontrol.fail2ban.script.centos.internal.centos

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_11.external.Fail2ban_0_11
import com.anrisoftware.sscontrol.utils.centos.external.CentosUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Fail2ban</i> service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Fail2ban_Centos extends Fail2ban_0_11 {

    abstract CentosUtils getCentos()

    SystemdUtils systemd

    void installPackages() {
        centos.installPackages(["epel-release"])
        centos.installPackages()
    }

    @Inject
    void setSystemd(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    def stopService() {
        systemd.stopServices()
    }

    def startService() {
        systemd.startServices()
    }

    def restartService() {
        systemd.restartServices()
    }

    def enableService() {
        systemd.enableServices()
    }

    @Override
    def getLog() {
        log
    }
}
