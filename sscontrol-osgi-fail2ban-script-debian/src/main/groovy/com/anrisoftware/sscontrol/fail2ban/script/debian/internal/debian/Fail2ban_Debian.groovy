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
package com.anrisoftware.sscontrol.fail2ban.script.debian.internal.debian

import static org.joda.time.Duration.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_9.external.Fail2ban_0_9
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
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
abstract class Fail2ban_Debian extends Fail2ban_0_9 {

    abstract DebianUtils getDebian()

    SystemdUtils systemd

    void installPackages() {
        debian.installPackages()
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
