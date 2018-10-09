package com.anrisoftware.sscontrol.registry.docker.service.internal;

import static com.anrisoftware.sscontrol.registry.docker.service.internal.RegistryImplLogger.m.addressSet;
import static com.anrisoftware.sscontrol.registry.docker.service.internal.RegistryImplLogger.m.portSet;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RegistryImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class RegistryImplLogger extends AbstractLogger {

    enum m {

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
     * Sets the context of the logger to {@link RegistryImpl}.
     */
    public RegistryImplLogger() {
        super(RegistryImpl.class);
    }

    void portSet(RegistryImpl remote, int port) {
        debug(portSet, port, remote);
    }

    void addressSet(RegistryImpl registry, URI address) {
        debug(addressSet, address, registry);
    }
}
