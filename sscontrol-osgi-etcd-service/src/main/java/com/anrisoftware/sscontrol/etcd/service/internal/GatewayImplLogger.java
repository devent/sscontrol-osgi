package com.anrisoftware.sscontrol.etcd.service.internal;

import static com.anrisoftware.sscontrol.etcd.service.internal.GatewayImplLogger.m.endpointAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link GatewayImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class GatewayImplLogger extends AbstractLogger {

    enum m {

        endpointAdded("Endpoint {} added to {}");

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
     * Sets the context of the logger to {@link GatewayImpl}.
     */
    public GatewayImplLogger() {
        super(GatewayImpl.class);
    }

    void advertiseAdded(GatewayImpl proxy, String endpoint) {
        debug(endpointAdded, endpoint, proxy);
    }

}
