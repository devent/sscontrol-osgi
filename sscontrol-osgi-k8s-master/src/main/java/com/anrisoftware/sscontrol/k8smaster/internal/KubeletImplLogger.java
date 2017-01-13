package com.anrisoftware.sscontrol.k8smaster.internal;

import static com.anrisoftware.sscontrol.k8smaster.internal.KubeletImplLogger._.preferredTypesAdded;
import static com.anrisoftware.sscontrol.k8smaster.internal.KubeletImplLogger._.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8smaster.external.Tls;

/**
 * Logging for {@link KubeletImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class KubeletImplLogger extends AbstractLogger {

    enum _ {

        tlsSet("TLS {} set for {}"),

        preferredTypesAdded("Preferred node address types {} added to {}");

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
     * Sets the context of the logger to {@link KubeletImpl}.
     */
    public KubeletImplLogger() {
        super(KubeletImpl.class);
    }

    void tlsSet(KubeletImpl kubelet, Tls tls) {
        debug(tlsSet, tls, kubelet);
    }

    void preferredTypesAdded(KubeletImpl kubelet, Object v) {
        debug(preferredTypesAdded, v, kubelet);
    }
}
