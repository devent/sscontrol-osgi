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
package com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8_debian.internal

import static org.joda.time.Duration.*

import com.anrisoftware.sscontrol.fail2ban.fail2ban_0_8.external.Ufw_Fail2ban_0_8

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class Ufw_Fail2ban_0_8_Debian extends Ufw_Fail2ban_0_8 {

    void installPackages() {
        log.info "Installing packages {}.", packages
        shell privileged: true, timeout: standardHours(1), "apt-get -y install ${packages.join(' ')}" with { //
            env "DEBIAN_FRONTEND=noninteractive" } call()
    }

    @Override
    def getLog() {
        log
    }
}