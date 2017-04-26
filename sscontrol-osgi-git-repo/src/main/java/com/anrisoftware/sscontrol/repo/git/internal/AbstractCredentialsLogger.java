package com.anrisoftware.sscontrol.repo.git.internal;

import static com.anrisoftware.sscontrol.repo.git.internal.AbstractCredentialsLogger.m.nameSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractCredentials}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AbstractCredentialsLogger extends AbstractLogger {

    enum m {

        nameSet("Name {} set for {}");

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
     * Sets the context of the logger to {@link AbstractCredentials}.
     */
    public AbstractCredentialsLogger() {
        super(AbstractCredentials.class);
    }

    void nameSet(AbstractCredentials credentials, String name) {
        debug(nameSet, name, credentials);
    }
}
