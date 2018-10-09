package com.anrisoftware.sscontrol.app.main.internal.main;

import static com.anrisoftware.sscontrol.app.main.internal.main.HostApplicationLogger._.felixStarted;
import static com.anrisoftware.sscontrol.app.main.internal.main.HostApplicationLogger._.felixStopped;

import java.util.Map;

import javax.inject.Singleton;

import org.apache.felix.framework.Felix;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link HostApplication}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostApplicationLogger extends AbstractLogger {

    enum _ {

        felixStarted("Felix started with {}"),

        felixStopped("Felix stopped: {}");

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
     * Sets the context of the logger to {@link HostApplication}.
     */
    public HostApplicationLogger() {
        super(HostApplication.class);
    }

    void felixStarted(Map<String, Object> configMap) {
        debug(felixStarted, configMap);
    }

    void felixStopped(Felix felix) {
        debug(felixStopped, felix);
    }
}
