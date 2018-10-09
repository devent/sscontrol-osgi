package com.anrisoftware.sscontrol.services.internal.targets;


import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractTargetsImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class AbstractTargetsImplLogger extends AbstractLogger {

	enum m {

        addHosts("Add hosts '{}':={} to {}");

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
     * Sets the context of the logger to {@link AbstractTargetsImpl}.
     */
    public AbstractTargetsImplLogger() {
        super(AbstractTargetsImpl.class);
    }

    public void addHosts(AbstractTargetsImpl<?, ?> targets, Object ssh, String group) {
		debug(m.addHosts, group, ssh, targets);
    }
}
