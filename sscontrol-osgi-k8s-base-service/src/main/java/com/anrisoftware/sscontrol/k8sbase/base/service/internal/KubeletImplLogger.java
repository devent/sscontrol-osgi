package com.anrisoftware.sscontrol.k8sbase.base.service.internal;

import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImplLogger.m.clientSet;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImplLogger.m.preferredTypesAdded;
import static com.anrisoftware.sscontrol.k8sbase.base.service.internal.KubeletImplLogger.m.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link KubeletImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class KubeletImplLogger extends AbstractLogger {

    enum m {

        tlsSet("TLS {} set for {}"),

        preferredTypesAdded("Preferred node address types {} added to {}"),

        clientSet("Client {} set for {}"),

        portSet("Port {} set for {}"),

        addressSet("Address {} set for {}");

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

    void clientSet(KubeletImpl kubelet, Tls client) {
        debug(clientSet, client, kubelet);
    }

    void portSet(KubeletImpl kubelet, int port) {
        debug(m.portSet, port, kubelet);
    }

    void addressSet(KubeletImpl kubelet, String address) {
        debug(m.addressSet, address, kubelet);
    }

}
