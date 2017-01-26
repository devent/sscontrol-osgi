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
package com.anrisoftware.sscontrol.sshd.debian.sshd_6.external

import com.anrisoftware.sscontrol.sshd.external.Sshd
import com.anrisoftware.sscontrol.sshd.sshd_6.external.Sshd_6

import groovy.util.logging.Slf4j

/**
 * Configures the <i>Sshd</i> 6.0 service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Sshd_6_Debian extends Sshd_6 {

    void installPackages() {
        log.info "Installing packages {}.", packages
        shell privileged: true, "apt-get -y install ${packages.join(' ')}" with { //
            env "DEBIAN_FRONTEND=noninteractive" } call()
    }

    @Override
    Sshd getService() {
        super.getService();
    }

    @Override
    def getLog() {
        log
    }
}
