package com.anrisoftware.sscontrol.docker.service.internal;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LoggingDriverImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class LoggingDriverImplLogger extends AbstractLogger {

    enum m {

        driverSet("Driver '{}' set for {}"),

        optsSet("Driver options {} set for {}");

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
     * Sets the context of the logger to {@link LoggingDriverImpl}.
     */
    public LoggingDriverImplLogger() {
        super(LoggingDriverImpl.class);
    }

    void driverSet(LoggingDriverImpl logging, String driver) {
        debug(m.driverSet, driver, logging);
    }

    void optsSet(LoggingDriverImpl logging, Map<String, Object> opts) {
        debug(m.optsSet, opts, logging);
    }

}
