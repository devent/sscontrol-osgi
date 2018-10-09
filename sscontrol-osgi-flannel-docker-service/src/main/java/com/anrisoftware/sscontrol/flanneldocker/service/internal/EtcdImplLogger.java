package com.anrisoftware.sscontrol.flanneldocker.service.internal;

import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.addressSet;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.endpointAdded;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.endpointsAdded;
import static com.anrisoftware.sscontrol.flanneldocker.service.internal.EtcdImplLogger.m.tlsSet;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
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

        tlsSet("TLS {} set for {}"),

        endpointAdded("Endpoint {} added to {}"),

        endpointsAdded("Endpoints {} added to {}"),

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
     * Sets the context of the logger to {@link EtcdImpl}.
     */
    public EtcdImplLogger() {
        super(EtcdImpl.class);
    }

    void tlsSet(EtcdImpl etcd, Tls tls) {
        debug(tlsSet, tls, etcd);
    }

    void endpointAdded(EtcdImpl etcd, Object endpoint) {
        debug(endpointAdded, endpoint, etcd);
    }

    void endpointsAdded(EtcdImpl etcd, List<?> list) {
        debug(endpointsAdded, list, etcd);
    }

    void addressSet(EtcdImpl etcd, Object address) {
        debug(addressSet, address, etcd);
    }
}
