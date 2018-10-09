package com.anrisoftware.sscontrol.hosts.service.internal;

import static com.anrisoftware.sscontrol.hosts.service.internal.HostsImplLogger.m.hostAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.hosts.service.external.Host;
import com.anrisoftware.sscontrol.hosts.service.external.Hosts;

/**
 * Logging for {@link HostsImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostsImplLogger extends AbstractLogger {

    enum m {

        hostAdded("Host {} added to {}");

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
     * Sets the context of the logger to {@link HostsImpl}.
     */
    public HostsImplLogger() {
        super(HostsImpl.class);
    }

    public void hostAdded(Hosts hosts, Host h) {
        debug(hostAdded, h, hosts);
    }

}
