package com.anrisoftware.sscontrol.hostname.service.internal;

import static com.anrisoftware.sscontrol.hostname.service.internal.HostnameImplLogger.m.hostnameSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.hostname.service.external.Hostname;

/**
 * Logging for {@link HostnameImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostnameImplLogger extends AbstractLogger {

    enum m {

        hostnameSet("Hostname '{}' set for {}");

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
     * Sets the context of the logger to {@link HostnameImpl}.
     */
    public HostnameImplLogger() {
        super(HostnameImpl.class);
    }

    public void hostnameSet(Hostname database, String name) {
        debug(hostnameSet, name, database);
    }

}
