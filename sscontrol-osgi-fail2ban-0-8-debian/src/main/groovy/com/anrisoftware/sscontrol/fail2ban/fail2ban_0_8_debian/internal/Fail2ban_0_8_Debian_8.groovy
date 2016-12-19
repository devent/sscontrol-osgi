/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal

import static com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal.Fail2ban_0_8_Debian_8_Service.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8.external.Ufw_Fail2ban_0_8
import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.external.Fail2ban_0_8_Debian

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Fail2ban 0.8</i> service for Debian 8.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Fail2ban_0_8_Debian_8 extends Fail2ban_0_8_Debian {

    @Inject
    Fail2ban_0_8_Debian_8_Properties debianPropertiesProvider

    @Inject
    Ufw_Fail2ban_0_8_Debian_8 ufwDebian

    @Override
    def run() {
        setupDefaults()
        installPackages()
        configureService()
        ufwDebian.run()
        restartService()
    }

    @Override
    Ufw_Fail2ban_0_8 getUfwScript() {
        ufwDebian.setChdir chdir
        ufwDebian.setPwd pwd
        ufwDebian.setSudoEnv sudoEnv
        ufwDebian.setEnv env
        ufwDebian.setScript this
        ufwDebian
    }

    @Override
    Properties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
