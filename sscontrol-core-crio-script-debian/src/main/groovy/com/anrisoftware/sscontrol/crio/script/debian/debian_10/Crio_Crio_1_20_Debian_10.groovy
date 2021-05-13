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
package com.anrisoftware.sscontrol.crio.script.debian.debian_10

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.crio.script.crio_1_20.Crio_1_20
import com.anrisoftware.sscontrol.utils.debian.external.DebianUtils
import com.anrisoftware.sscontrol.utils.debian.external.Debian_10_UtilsFactory
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtils
import com.anrisoftware.sscontrol.utils.systemd.external.SystemdUtilsFactory

import groovy.util.logging.Slf4j

/**
 * CRI-O 1.20 for Debian 10.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class Crio_Crio_1_20_Debian_10 extends Crio_1_20 {

    @Inject
    Crio_1_20_Debian_10_Properties propertiesProvider

    DebianUtils debian

    SystemdUtils systemd

    @Override
    def run() {
    }

    def enableService() {
        systemd.enableServices()
        systemd.restartServices()
    }

    @Inject
    void setDebian(Debian_10_UtilsFactory factory) {
        this.debian = factory.create this
    }

    @Inject
    void setSystemd(SystemdUtilsFactory factory) {
        this.systemd = factory.create this
    }

    @Override
    ContextProperties getDefaultProperties() {
        propertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
