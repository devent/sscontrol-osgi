package com.anrisoftware.sscontrol.repo.git.internal;

import static com.anrisoftware.sscontrol.repo.git.internal.SshCredentialsImplLogger.m.keySet;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SshCredentialsImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class SshCredentialsImplLogger extends AbstractLogger {

    enum m {

        keySet("Key {} set for {}");

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
     * Sets the context of the logger to {@link SshCredentialsImpl}.
     */
    public SshCredentialsImplLogger() {
        super(SshCredentialsImpl.class);
    }

    void keySet(SshCredentialsImpl credentials, URI key) {
        debug(keySet, key, credentials);
    }
}
