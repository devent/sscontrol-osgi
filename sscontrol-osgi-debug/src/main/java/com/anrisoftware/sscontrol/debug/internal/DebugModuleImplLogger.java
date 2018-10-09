package com.anrisoftware.sscontrol.debug.internal;

import static com.anrisoftware.sscontrol.debug.internal.DebugModuleImplLogger._.debugSet;
import static com.anrisoftware.sscontrol.debug.internal.DebugModuleImplLogger._.propertyPut;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link DebugModuleImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DebugModuleImplLogger extends AbstractLogger {

    enum _ {

        debugSet("Module {} set for {}."),

        propertyPut("Property '{}'={} put for {}.");

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
     * Sets the context of the logger to {@link DebugModuleImpl}.
     */
    public DebugModuleImplLogger() {
        super(DebugModuleImpl.class);
    }

    void debugSet(DebugModuleImpl module, Map<String, Object> args) {
        debug(debugSet, args, module);
    }

    void propertyPut(DebugModuleImpl module, String property, Object value) {
        debug(propertyPut, property, value, module);
    }
}
