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

import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.advertiseAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.authenticationAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.clusterAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.listenAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.stateSet;
import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.tlsSet;
import static com.anrisoftware.sscontrol.etcd.service.internal.PeerImplLogger.m.tokenSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.etcd.service.external.Authentication;
import com.anrisoftware.sscontrol.etcd.service.external.Binding;
import com.anrisoftware.sscontrol.etcd.service.external.Cluster;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link PeerImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class PeerImplLogger extends AbstractLogger {

    enum m {

        advertiseAdded("Advertise {} added to {}"),

        authenticationAdded("Authentication {} added to {}"),

        tokenSet("Token {} set for {}"),

        stateSet("State {} set for {}"),

        listenAdded("Authentication {} added to {}"),

        clusterAdded("Cluster {} added to {}"),

        tlsSet("Tls {} set for {}");

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
     * Sets the context of the logger to {@link PeerImpl}.
     */
    public PeerImplLogger() {
        super(PeerImpl.class);
    }

    void advertiseAdded(PeerImpl peer, Binding binding) {
        debug(advertiseAdded, binding, peer);
    }

    void listenAdded(PeerImpl peer, Binding binding) {
        debug(listenAdded, binding, peer);
    }

    void authenticationAdded(PeerImpl peer, Authentication auth) {
        debug(authenticationAdded, auth, peer);
    }

    void tokenSet(PeerImpl peer, String token) {
        debug(tokenSet, token, peer);
    }

    void stateSet(PeerImpl peer, String state) {
        debug(stateSet, state, peer);
    }

    void clusterAdded(PeerImpl peer, Cluster cluster) {
        debug(clusterAdded, cluster, peer);
    }

    void tlsSet(PeerImpl peer, Tls tls) {
        debug(tlsSet, tls, peer);
    }
}
