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
package com.anrisoftware.sscontrol.k8sbase.script.upstream.external.k8s_1_13.linux

import javax.inject.Inject

import com.anrisoftware.sscontrol.types.ssh.external.SshHost
import com.google.inject.assistedinject.Assisted

/**
 * Returns the host, protocol and port of the target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class Addresses {

    private List<Object> addresses

    private Object parent

    @Inject
    Addresses(@Assisted Object parent, @Assisted List<Object> addresses) {
        this.parent = parent
        this.addresses = new ArrayList(addresses)
    }

    List getHosts() {
        addresses.inject([]) { result, it ->
            if (it instanceof List) {
                it.each { result << toAddress(it)  }
            }
            else {
                result << toAddress(it)
            }
        }
    }

    private Map toAddress(def target) {
        if (target instanceof SshHost) {
            return [host: target.host, protocol: parent.protocol, port: parent.port]
        } else {
            return getAddress(target)
        }
    }

    private Map getAddress(String address) {
        def uri = new URI(address)
        def host = uri.host ? uri.host : uri.path
        def proto = uri.scheme ? uri.scheme : parent.protocol
        def port = uri.port != -1 ? uri.port : parent.port
        [host: host, protocol: proto, port: port]
    }
}
