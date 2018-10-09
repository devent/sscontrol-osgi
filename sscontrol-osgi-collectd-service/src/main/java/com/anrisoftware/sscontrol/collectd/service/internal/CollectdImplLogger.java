package com.anrisoftware.sscontrol.collectd.service.internal;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.collectd.service.external.Config;

/**
 * Logging for {@link CollectdImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class CollectdImplLogger extends AbstractLogger {

    enum m {

        configAdded("Config {} added to {}");

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
     * Sets the context of the logger to {@link CollectdImpl}.
     */
    public CollectdImplLogger() {
        super(CollectdImpl.class);
    }

    void configAdded(CollectdImpl icinga, Config config) {
        debug(m.configAdded, config, icinga);
    }

}
