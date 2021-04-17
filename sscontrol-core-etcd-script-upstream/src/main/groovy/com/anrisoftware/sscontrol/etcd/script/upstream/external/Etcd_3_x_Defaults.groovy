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
package com.anrisoftware.sscontrol.etcd.script.upstream.external

import javax.inject.Inject

import com.anrisoftware.sscontrol.etcd.service.external.BindingFactory
import com.anrisoftware.sscontrol.etcd.service.external.Etcd
import com.anrisoftware.sscontrol.groovy.script.external.ScriptBase
import com.anrisoftware.sscontrol.tls.external.Tls

import groovy.util.logging.Slf4j

/**
 * Setups the defaults for the <i>Etcd</i> 3.x service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
abstract class Etcd_3_x_Defaults extends ScriptBase {

    @Inject
    BindingFactory bindingFactory

    def setupDefaults() {
        Etcd service = service
        if (!service.debugLogging.modules['debug']) {
            service.debug 'debug', level: defaultDebugLogLevel
        }
        if (service.bindings.size() == 0) {
            service.bindings << bindingFactory.create()
        }
        if (!service.bindings[0].address) {
            service.bindings[0].address = defaultBindingAddress
        }
        if (service.advertises.size() == 0) {
            service.advertises << bindingFactory.create()
        }
        if (!service.advertises[0].address) {
            service.advertises[0].address = defaultAdvertiseAddress
        }
        if (!service.memberName) {
            service.memberName = defaultMemberName
        }
        if (!service.tls.caName) {
            service.tls.caName = defaultServerTlsCaName
        }
        if (!service.tls.certName) {
            service.tls.certName = defaultServerTlsCertName
        }
        if (!service.tls.keyName) {
            service.tls.keyName = defaultServerTlsKeyName
        }
        if (service.client && service.client.tls) {
            if (!service.client.tls.caName) {
                service.client.tls.caName = defaultClientTlsCaName
            }
            if (!service.client.tls.certName) {
                service.client.tls.certName = defaultClientTlsCertName
            }
            if (!service.client.tls.keyName) {
                service.client.tls.keyName = defaultClientTlsKeyName
            }
        }
        service.authentications.findAll { it.tls  } each {
            Tls tls = it.tls
            if (!tls.caName) {
                tls.caName = defaultClientAuthenticationTlsCaName[it.type]
            }
            if (!tls.certName) {
                tls.certName = defaultClientAuthenticationTlsCertName[it.type]
            }
            if (!tls.keyName) {
                tls.keyName = defaultClientAuthenticationTlsKeyName[it.type]
            }
        }
        if (service.peer) {
            if (!service.peer.tls.caName) {
                service.peer.tls.caName = defaultPeerTlsCaName
            }
            if (!service.peer.tls.certName) {
                service.peer.tls.certName = defaultPeerTlsCertName
            }
            if (!service.peer.tls.keyName) {
                service.peer.tls.keyName = defaultPeerTlsKeyName
            }
            service.peer.authentications.findAll { it.tls  } each {
                Tls tls = it.tls
                if (!tls.caName) {
                    tls.caName = defaultPeerAuthenticationTlsCaName[it.type]
                }
                if (!tls.certName) {
                    tls.certName = defaultPeerAuthenticationTlsCertName[it.type]
                }
                if (!tls.keyName) {
                    tls.keyName = defaultPeerAuthenticationTlsKeyName[it.type]
                }
            }
        }
    }

    def getDefaultDebugLogLevel() {
        properties.getNumberProperty 'default_debug_log_level', defaultProperties intValue()
    }

    URI getDefaultBindingAddress() {
        properties.getURIProperty 'default_binding_address', defaultProperties
    }

    URI getDefaultAdvertiseAddress() {
        properties.getURIProperty 'default_advertise_address', defaultProperties
    }

    def getDefaultMemberName() {
        properties.getProperty 'default_member_name', defaultProperties
    }

    def getDefaultServerTlsCaName() {
        properties.getProperty 'default_tls_ca_name', defaultProperties
    }

    def getDefaultServerTlsCertName() {
        properties.getProperty 'default_tls_cert_name', defaultProperties
    }

    def getDefaultServerTlsKeyName() {
        properties.getProperty 'default_tls_key_name', defaultProperties
    }

    def getDefaultClientTlsCaName() {
        properties.getProperty 'default_client_tls_ca_name', defaultProperties
    }

    def getDefaultClientTlsCertName() {
        properties.getProperty 'default_client_tls_cert_name', defaultProperties
    }

    def getDefaultClientTlsKeyName() {
        properties.getProperty 'default_client_tls_key_name', defaultProperties
    }

    def getDefaultPeerTlsCaName() {
        properties.getProperty 'default_peer_tls_ca_name', defaultProperties
    }

    def getDefaultPeerTlsCertName() {
        properties.getProperty 'default_peer_tls_cert_name', defaultProperties
    }

    def getDefaultPeerTlsKeyName() {
        properties.getProperty 'default_peer_tls_key_name', defaultProperties
    }

    Map getDefaultClientAuthenticationTlsCaName() {
        def s = properties.getProperty 'default_authentication_tls_ca_name', defaultProperties
        Eval.me s
    }

    Map getDefaultClientAuthenticationTlsCertName() {
        def s = properties.getProperty 'default_authentication_tls_cert_name', defaultProperties
        Eval.me s
    }

    Map getDefaultClientAuthenticationTlsKeyName() {
        def s = properties.getProperty 'default_authentication_tls_key_name', defaultProperties
        Eval.me s
    }

    Map getDefaultPeerAuthenticationTlsCaName() {
        def s = properties.getProperty 'default_peer_authentication_tls_ca_name', defaultProperties
        Eval.me s
    }

    Map getDefaultPeerAuthenticationTlsCertName() {
        def s = properties.getProperty 'default_peer_authentication_tls_cert_name', defaultProperties
        Eval.me s
    }

    Map getDefaultPeerAuthenticationTlsKeyName() {
        def s = properties.getProperty 'default_peer_authentication_tls_key_name', defaultProperties
        Eval.me s
    }

    @Override
    def getLog() {
        log
    }
}
