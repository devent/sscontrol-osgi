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
package com.anrisoftware.sscontrol.nfs.script.nfs_1_3.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.nfs.service.external.Export
import com.anrisoftware.sscontrol.nfs.service.external.Host
import com.anrisoftware.sscontrol.nfs.service.external.Nfs
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils

import groovy.util.logging.Slf4j

/**
 * Nfs 1.3 Ufw.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Nfs_1_3_Ufw extends ScriptBase {

    UfwUtils ufw

    @Inject
    void setUfwLinuxUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create(this)
    }

    /**
     * Configures Ufw to allow connections from Nfs hosts.
     */
    def configureFilewall() {
        Nfs service = this.service
        if (!ufw.ufwActive) {
            return
        }
        service.exports.each { Export export ->
            export.hosts.each { Host host ->
            shell privileged: true, """
ufw allow from ${InetAddress.getByName(host.name).hostAddress} to any port nfs
""" call()
            }
        }
    }

    @Override
    def getLog() {
        log
    }
}
