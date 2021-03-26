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
package com.anrisoftware.sscontrol.etcd.service.internal;

import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.advertiseAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.authenticationAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.bindingAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.clientSet;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.gatewaySet;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.memberNameSet;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.peerSet;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.proxySet;
import static com.anrisoftware.sscontrol.etcd.service.internal.EtcdImplLogger.m.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.etcd.service.external.Authentication;
import com.anrisoftware.sscontrol.etcd.service.external.Binding;
import com.anrisoftware.sscontrol.etcd.service.external.Client;
import com.anrisoftware.sscontrol.etcd.service.external.Gateway;
import com.anrisoftware.sscontrol.etcd.service.external.Peer;
import com.anrisoftware.sscontrol.etcd.service.external.Proxy;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link EtcdImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class EtcdImplLogger extends AbstractLogger {

    enum m {

        bindingAdded("Binding {} added for {}"),

        memberNameSet("Member name {} set for {}"),

        advertiseAdded("Advertise {} added for {}"),

        tlsSet("TLS {} set for {}"),

        authenticationAdded("Authentication {} added for {}"),

        peerSet("Peer {} set for {}"),

        clientSet("Client {} set for {}"),

        proxySet("Proxy {} set for {}"),

        gatewaySet("Gateway {} set for {}");

        private String name;

        private m(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link EtcdImpl}.
     */
    public EtcdImplLogger() {
        super(EtcdImpl.class);
    }

    void bindingAdded(EtcdImpl etcd, Binding binding) {
        debug(bindingAdded, binding, etcd);
    }

    void memberNameSet(EtcdImpl etcd, String member) {
        debug(memberNameSet, member, etcd);
    }

    void advertiseAdded(EtcdImpl etcd, Binding binding) {
        debug(advertiseAdded, binding, etcd);
    }

    void tlsSet(EtcdImpl etcd, Tls tls) {
        debug(tlsSet, tls, etcd);
    }

    void authenticationAdded(EtcdImpl etcd, Authentication auth) {
        debug(authenticationAdded, auth, etcd);
    }

    void peerSet(EtcdImpl etcd, Peer peer) {
        debug(peerSet, peer, etcd);
    }

    void clientSet(EtcdImpl etcd, Client client) {
        debug(clientSet, client, etcd);
    }

    void proxySet(EtcdImpl etcd, Proxy proxy) {
        debug(proxySet, proxy, etcd);
    }

    void gatewaySet(EtcdImpl etcd, Gateway gateway) {
        debug(gatewaySet, gateway, etcd);
    }
}
