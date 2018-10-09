package com.anrisoftware.sscontrol.shell.internal;

import static com.anrisoftware.sscontrol.shell.internal.ShellImplLogger.m.scriptAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.shell.external.Script;

/**
 * Logging for {@link ShellImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ShellImplLogger extends AbstractLogger {

    enum m {

        scriptAdded("Script added to {}: {}");

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
     * Sets the context of the logger to {@link ShellImpl}.
     */
    public ShellImplLogger() {
        super(ShellImpl.class);
    }

    void scriptAdded(ShellImpl shell, Script script) {
        debug(scriptAdded, shell, script);
    }

}
