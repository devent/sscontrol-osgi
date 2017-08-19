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

import com.anrisoftware.sscontrol.fail2ban.script.fail2ban_0_9.external.Ufw_Fail2ban_0_9
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
abstract class Ufw_Fail2ban_0_9_Debian extends Ufw_Fail2ban_0_9 {

    abstract DebianUtils getDebian()

    def installPackages() {
        debian.installPackages()
    }

    @Override
    def getLog() {
        log
    }
}
