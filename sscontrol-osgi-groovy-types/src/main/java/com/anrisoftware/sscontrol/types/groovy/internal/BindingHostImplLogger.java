package com.anrisoftware.sscontrol.types.groovy.internal;

import static com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImplLogger.m.hostAddAdded;
import static com.anrisoftware.sscontrol.types.groovy.internal.BindingHostImplLogger.m.hostRemoveAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.misc.external.BindingHost.Host;

/**
 * Logging for {@link BindingHostImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class BindingHostImplLogger extends AbstractLogger {

    enum m {

        hostAddAdded("Host to add {} added to {}"),

        hostRemoveAdded("Host to remove {} added to {}");

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
     * Sets the context of the logger to {@link BindingHostImpl}.
     */
    public BindingHostImplLogger() {
        super(BindingHostImpl.class);
    }

    void hostAddAdded(BindingHostImpl binding, Host host) {
        debug(hostAddAdded, host, binding);
    }

    void hostRemoveAdded(BindingHostImpl binding, Host host) {
        debug(hostRemoveAdded, host, binding);
    }
}
