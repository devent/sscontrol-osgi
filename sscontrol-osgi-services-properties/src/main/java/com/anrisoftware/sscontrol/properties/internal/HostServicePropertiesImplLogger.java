package com.anrisoftware.sscontrol.properties.internal;

import static com.anrisoftware.sscontrol.properties.internal.HostServicePropertiesImplLogger._.propertyAdded;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link HostServicePropertiesImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostServicePropertiesImplLogger extends AbstractLogger {

    enum _ {

        propertyAdded("Property {} = {} added to {}");

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
     * Sets the context of the logger to {@link HostServicePropertiesImpl}.
     */
    public HostServicePropertiesImplLogger() {
        super(HostServicePropertiesImpl.class);
    }

    void propertyAdded(HostServicePropertiesImpl properties, String name,
            Object value) {
        debug(propertyAdded, name, value, properties);
    }
}
