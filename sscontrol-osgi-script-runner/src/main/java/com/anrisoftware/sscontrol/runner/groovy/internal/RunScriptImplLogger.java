package com.anrisoftware.sscontrol.runner.groovy.internal;

import static com.anrisoftware.sscontrol.runner.groovy.internal.RunScriptImplLogger.m.scriptNotFound;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.host.external.ScriptInfo;

/**
 * Logging for {@link RunScriptImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class RunScriptImplLogger extends AbstractLogger {

    enum m {

        scriptNotFound("Service '{}' script not found for '{}'");

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
     * Sets the context of the logger to {@link RunScriptImpl}.
     */
    public RunScriptImplLogger() {
        super(RunScriptImpl.class);
    }

    void scriptNotFound(String name, ScriptInfo system) {
        debug(scriptNotFound, name, system);
    }
}
