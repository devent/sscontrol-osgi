package com.anrisoftware.sscontrol.etcd.service.internal;

import static com.anrisoftware.sscontrol.etcd.service.internal.ClientImplLogger.m.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link ClientImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ClientImplLogger extends AbstractLogger {

    enum m {

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
     * Sets the context of the logger to {@link ClientImpl}.
     */
    public ClientImplLogger() {
        super(ClientImpl.class);
    }

    void tlsSet(ClientImpl peer, Tls tls) {
        debug(tlsSet, tls, peer);
    }
}
