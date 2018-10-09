package com.anrisoftware.sscontrol.docker.service.internal;

import static com.anrisoftware.sscontrol.docker.service.internal.MirrorImplLogger.m.hostSet;
import static com.anrisoftware.sscontrol.docker.service.internal.MirrorImplLogger.m.tlsSet;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Logging for {@link MirrorImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class MirrorImplLogger extends AbstractLogger {

    enum m {

        hostSet("Host {} set for {}"),

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
     * Sets the context of the logger to {@link MirrorImpl}.
     */
    public MirrorImplLogger() {
        super(MirrorImpl.class);
    }

    void hostSet(MirrorImpl registry, URI host) {
        debug(hostSet, host, registry);
    }

    void tlsSet(MirrorImpl registry, Tls tls) {
        debug(tlsSet, tls, registry);
    }

}
