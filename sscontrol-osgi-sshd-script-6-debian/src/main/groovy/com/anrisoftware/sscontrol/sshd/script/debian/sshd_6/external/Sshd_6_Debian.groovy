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
package com.anrisoftware.sscontrol.sshd.script.debian.sshd_6.external

import com.anrisoftware.sscontrol.sshd.script.linux.sshd_6.external.Abstract_Sshd_6_Systemd
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> 6.0 service for Debian.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Sshd_6_Debian extends Abstract_Sshd_6_Systemd {

    abstract DebianUtils getDebian()

    void installPackages() {
        debian.installPackages()
    }

    @Override
    def getLog() {
        log
    }
}
