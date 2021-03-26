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
package com.anrisoftware.sscontrol.haproxy.script.haproxy_1_8.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.haproxy.service.external.HAProxy
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils
import com.anrisoftware.sscontrol.haproxy.service.external.Proxy

import groovy.util.logging.Slf4j

/**
 * HAProxy 1.8 Ufw.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class HAProxy_1_8_Ufw extends ScriptBase {

    UfwUtils ufw

    @Inject
    void setUfwLinuxUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create(this)
    }

    /**
     * Configures Ufw to allow connections to the frontends.
     */
    def configureFilewall() {
        HAProxy service = this.service
        if (!ufw.ufwActive) {
            return
        }
        service.proxies.each { Proxy proxy ->
            def address = proxy.frontend.address == "*" ? "any" : proxy.frontend.address
            shell privileged: true, """
ufw allow from ${target.hostAddress} to ${address} port ${proxy.frontend.port}
""" call()
        }
    }

    @Override
    def getLog() {
        log
    }
}
