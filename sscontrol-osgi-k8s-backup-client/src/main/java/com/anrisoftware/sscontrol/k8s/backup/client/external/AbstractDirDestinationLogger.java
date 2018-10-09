package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractDirDestination}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final public class AbstractDirDestinationLogger extends AbstractLogger {

    enum m {

        destSet("Directory {} set for {}"),

        argumentsSet("Arguments {} set for {}");

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
     * Sets the context of the logger to {@link AbstractDirDestination}.
     */
    AbstractDirDestinationLogger() {
        super(AbstractDirDestination.class);
    }

    void dirSet(AbstractDirDestination dest, File dir) {
        debug(m.destSet, dir, dest);
    }

    void argumentsSet(AbstractDirDestination dest, String arguments) {
        debug(m.argumentsSet, arguments, dest);
    }
}
