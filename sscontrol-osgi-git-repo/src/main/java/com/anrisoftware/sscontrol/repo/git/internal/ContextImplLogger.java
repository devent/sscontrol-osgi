package com.anrisoftware.sscontrol.repo.git.internal;

import static com.anrisoftware.sscontrol.repo.git.internal.ContextImplLogger.m.nameSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ContextImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ContextImplLogger extends AbstractLogger {

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
     * Sets the context of the logger to {@link ContextImpl}.
     */
    public ContextImplLogger() {
        super(ContextImpl.class);
    }

    void nameSet(ContextImpl cluster, String name) {
        debug(nameSet, name, cluster);
    }
}
