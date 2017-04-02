package com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal;

import static com.anrisoftware.sscontrol.k8smonitoringcluster.heapsterinfluxdbgrafana.internal.ClusterImplLogger.m.nameSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ClusterImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class ClusterImplLogger extends AbstractLogger {

    enum m {

        nameSet("Name {} set for {}");

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
     * Sets the context of the logger to {@link ClusterImpl}.
     */
    public ClusterImplLogger() {
        super(ClusterImpl.class);
    }

    void nameSet(ClusterImpl cluster, String name) {
        debug(nameSet, name, cluster);
    }
}
