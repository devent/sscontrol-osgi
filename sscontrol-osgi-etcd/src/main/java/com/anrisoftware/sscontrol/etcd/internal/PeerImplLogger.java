package com.anrisoftware.sscontrol.etcd.internal;

import static com.anrisoftware.sscontrol.etcd.internal.PeerImplLogger._.advertiseAdded;
import static com.anrisoftware.sscontrol.etcd.internal.PeerImplLogger._.authenticationAdded;
import static com.anrisoftware.sscontrol.etcd.internal.PeerImplLogger._.clusterAdded;
import static com.anrisoftware.sscontrol.etcd.internal.PeerImplLogger._.listenAdded;
import static com.anrisoftware.sscontrol.etcd.internal.PeerImplLogger._.stateSet;
import static com.anrisoftware.sscontrol.etcd.internal.PeerImplLogger._.tokenSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.etcd.external.Authentication;
import com.anrisoftware.sscontrol.etcd.external.Binding;
import com.anrisoftware.sscontrol.etcd.external.Cluster;

/**
 * Logging for {@link PeerImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class PeerImplLogger extends AbstractLogger {

    enum _ {

        advertiseAdded("Advertise {} added to {}"),

        authenticationAdded("Authentication {} added to {}"),

        tokenSet("Token {} set for {}"),

        stateSet("State {} set for {}"),

        listenAdded("Authentication {} added to {}"),

        clusterAdded("Cluster {} added to {}");

        private String name;

        private _(String name) {
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
}
