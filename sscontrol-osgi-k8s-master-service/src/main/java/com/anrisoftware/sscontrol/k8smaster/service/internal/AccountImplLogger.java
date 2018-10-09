package com.anrisoftware.sscontrol.k8smaster.service.internal;

import static com.anrisoftware.sscontrol.k8smaster.service.internal.AccountImplLogger.m.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link AccountImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AccountImplLogger extends AbstractLogger {

    enum m {

        tlsSet("TLS {} set for {}");

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
     * Sets the context of the logger to {@link AccountImpl}.
     */
    public AccountImplLogger() {
        super(AccountImpl.class);
    }

    void tlsSet(AccountImpl kubelet, Tls tls) {
        debug(tlsSet, tls, kubelet);
    }

}
