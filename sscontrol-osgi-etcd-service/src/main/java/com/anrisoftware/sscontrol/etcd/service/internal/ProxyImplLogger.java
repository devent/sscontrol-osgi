package com.anrisoftware.sscontrol.etcd.service.internal;

import static com.anrisoftware.sscontrol.etcd.service.internal.ProxyImplLogger.m.endpointAdded;
import static com.anrisoftware.sscontrol.etcd.service.internal.ProxyImplLogger.m.namespaceSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ProxyImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ProxyImplLogger extends AbstractLogger {

    enum m {

        endpointAdded("Endpoint {} added to {}"),

        namespaceSet("Namespace {} set for {}");

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
     * Sets the context of the logger to {@link ProxyImpl}.
     */
    public ProxyImplLogger() {
        super(ProxyImpl.class);
    }

    void advertiseAdded(ProxyImpl proxy, String endpoint) {
        debug(endpointAdded, endpoint, proxy);
    }

    void namespaceSet(ProxyImpl proxy, String namespace) {
        debug(namespaceSet, namespace, proxy);
    }
}
