package com.anrisoftware.sscontrol.repo.git.service.internal;

import static com.anrisoftware.sscontrol.repo.git.service.internal.RemoteImplLogger.m.uriSet;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RemoteImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class RemoteImplLogger extends AbstractLogger {

    enum m {

        uriSet("URI {} set for {}");

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
     * Sets the context of the logger to {@link RemoteImpl}.
     */
    public RemoteImplLogger() {
        super(RemoteImpl.class);
    }

    void uriSet(RemoteImpl remote, URI uri) {
        debug(uriSet, uri, remote);
    }
}
