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
package com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2

import static com.anrisoftware.sscontrol.etcd.script.debian.internal.debian_9.etcd_3_2.EtcdDebianService.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.etcd.service.external.Etcd
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase

import groovy.util.logging.Slf4j

/**
 * Configures the Ufw firewall.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class EtcdUfw extends ScriptBase {

    @Inject
    EtcdDebianProperties debianPropertiesProvider

    @Override
    Object run() {
        if (!ufwActive) {
            log.debug 'No Ufw available.'
            return
        }
        updateFirewall()
    }

    boolean isUfwActive() {
        try {
            def out = shell privileged: true, outString: true, "ufw status" call() out
            return (out =~ /Status: active.*/)
        } catch (e) {
            return false
        }
    }

    def updateFirewall() {
        Etcd service = this.service
        String listenAddress = listenAddress
        List peersAddresses = peersAddresses
        if (!listenAddress) {
            return
        }
        if (peersAddresses.empty) {
            return
        }
        shell st: """
<vars.peers:{p|ufw allow from <p> to <vars.listen> port 2379};separator="\\n">
<vars.peers:{p|ufw allow from <p> to <vars.listen> port 2380};separator="\\n">
""", vars: [peers: peersAddresses, listen: listenAddress] call()
    }

    String getListenAddress() {
        Etcd service = this.service
        def listens = service.peer.listens
        if (listens.size() > 0) {
            def address = InetAddress.getByName(service.peer.listens[0].address.host)
            return address.hostAddress
        } else {
            return null
        }
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

    @Override
    String getSystemName() {
        SYSTEM_NAME
    }

    @Override
    String getSystemVersion() {
        SYSTEM_VERSION
    }
}
