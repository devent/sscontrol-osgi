package com.anrisoftware.sscontrol.debug.internal;

import static com.anrisoftware.sscontrol.debug.internal.DebugLoggingImplLogger._.modulePut;
import static com.anrisoftware.sscontrol.debug.internal.DebugLoggingImplLogger._.moduleRemoved;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.misc.external.DebugModule;

/**
 * Logging for {@link DebugLoggingImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DebugLoggingImplLogger extends AbstractLogger {

    enum _ {

        modulePut("Module {} put to {}"),

        moduleRemoved("Module '{}' removed from {}");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link DebugLoggingImpl}.
     */
    public DebugLoggingImplLogger() {
        super(DebugLoggingImpl.class);
    }

    void modulePut(DebugLoggingImpl debug, DebugModule module) {
        debug(modulePut, module, debug);
    }

    void moduleRemoved(DebugLoggingImpl debug, String name) {
        debug(moduleRemoved, name, debug);
    }

}
