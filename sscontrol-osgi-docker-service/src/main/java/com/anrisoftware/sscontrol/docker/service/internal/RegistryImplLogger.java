package com.anrisoftware.sscontrol.docker.service.internal;

import static com.anrisoftware.sscontrol.docker.service.internal.RegistryImplLogger.m.mirrorHostSet;
import static com.anrisoftware.sscontrol.docker.service.internal.RegistryImplLogger.m.tlsSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.docker.service.external.Mirror;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link RegistryImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class RegistryImplLogger extends AbstractLogger {

    enum m {

        mirrorHostSet("Mirror host {} added to {}"),

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
     * Sets the context of the logger to {@link RegistryImpl}.
     */
    public RegistryImplLogger() {
        super(RegistryImpl.class);
    }

    void mirrorAdded(RegistryImpl registry, Mirror mirror) {
        debug(mirrorHostSet, mirror, registry);
    }

    void tlsSet(RegistryImpl registry, Tls tls) {
        debug(tlsSet, tls, registry);
    }
}
