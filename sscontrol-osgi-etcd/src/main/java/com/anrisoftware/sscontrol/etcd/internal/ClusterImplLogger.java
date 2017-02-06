package com.anrisoftware.sscontrol.etcd.internal;

import static com.anrisoftware.sscontrol.etcd.internal.ClusterImplLogger._.addressSet;
import static com.anrisoftware.sscontrol.etcd.internal.ClusterImplLogger._.nameSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.etcd.external.Binding;

/**
 * Logging for {@link ClusterImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ClusterImplLogger extends AbstractLogger {

    enum _ {

        nameSet("Name {} set for {}"),

        addressSet("Address {} set for {}");

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
     * Sets the context of the logger to {@link ClusterImpl}.
     */
    public ClusterImplLogger() {
        super(ClusterImpl.class);
    }

    void nameSet(ClusterImpl cluster, String name) {
        debug(nameSet, name, cluster);
    }

    void addressSet(ClusterImpl cluster, Binding binding) {
        debug(addressSet, binding, cluster);
    }
}
