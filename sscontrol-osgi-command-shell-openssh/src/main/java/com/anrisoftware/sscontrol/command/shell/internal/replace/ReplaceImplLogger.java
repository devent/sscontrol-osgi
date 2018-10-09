package com.anrisoftware.sscontrol.command.shell.internal.replace;

import static com.anrisoftware.sscontrol.command.shell.internal.replace.ReplaceImplLogger.m.lineAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ReplaceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ReplaceImplLogger extends AbstractLogger {

    enum m {

        lineAdded("Line {} added to {}");

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
     * Sets the context of the logger to {@link ReplaceImpl}.
     */
    public ReplaceImplLogger() {
        super(ReplaceImpl.class);
    }

    void lineAdded(ReplaceImpl replace, ReplaceLine line) {
        debug(lineAdded, line, replace);
    }
}
