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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_10.etcd_3_4

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.service.external.Etcd
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwLinuxUtilsFactory
import com.anrisoftware.sscontrol.utils.ufw.linux.external.UfwUtils

import groovy.util.logging.Slf4j

/**
 * Configures the Ufw firewall.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class UfwEtcd_3_4_Debian_10 extends ScriptBase {

    @Inject
    Etcd_3_4_Debian_10_Properties debianPropertiesProvider

    UfwUtils ufw

    @Inject
    void setUfwUtilsFactory(UfwLinuxUtilsFactory factory) {
        this.ufw = factory.create this
    }

    @Override
    Object run() {
        Etcd service = this.service
        if (!ufw.active) {
            log.debug 'No Ufw available.'
            return
        }
        if (!service.peer) {
            log.debug 'No peer available.'
            return
        }
        if (service.peer.listens.empty) {
            log.debug 'No peer available.'
            return
        }
        updateFirewall()
    }

    def updateFirewall() {
        Etcd service = this.service
        shell privileged: true, st: """
<vars.peers:{p|ufw allow from <p> to <vars.listen> port 2379};separator="\\n">
<vars.peers:{p|ufw allow from <p> to <vars.listen> port 2380};separator="\\n">
""", vars: [peers: peersAddresses, listen: listenAddress] call()
    }

    String getListenAddress() {
        Etcd service = this.service
        def listens = service.peer.listens
        def address = InetAddress.getByName(service.peer.listens[0].address.host)
        return address.hostAddress
    }

    List getPeersAddresses() {
        Etcd service = this.service
        service.peer.clusters.inject([]) { acc, val ->
            def address = InetAddress.getByName(val.address.address.host)
            acc << address.hostAddress
        }
    }

    @Override
    ContextProperties getDefaultProperties() {
        debianPropertiesProvider.get()
    }

    @Override
    def getLog() {
        log
    }
}
