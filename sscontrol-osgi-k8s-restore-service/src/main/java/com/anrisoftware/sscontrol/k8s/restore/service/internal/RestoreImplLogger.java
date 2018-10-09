package com.anrisoftware.sscontrol.k8s.restore.service.internal;

import static com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImplLogger.m.clientSet;
import static com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImplLogger.m.serviceSet;
import static com.anrisoftware.sscontrol.k8s.restore.service.internal.RestoreImplLogger.m.sourceSet;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

/**
 * Logging for {@link RestoreImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class RestoreImplLogger extends AbstractLogger {

    enum m {

        clustersAdded("Clusters {} added to {}"),

        serviceSet("Service {} set for {}"),

        sourceSet("Source {} set for {}"),

		clientSet("Client {} set for {}"),

		dryrunSet("Dry-run {} set for {}");

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
     * Sets the context of the logger to {@link RestoreImpl}.
     */
    RestoreImplLogger() {
        super(RestoreImpl.class);
    }

    void clustersAdded(RestoreImpl from, List<ClusterHost> list) {
        debug(clustersAdded, list, from);
    }

    void serviceSet(RestoreImpl backup, Service service) {
        debug(serviceSet, service, backup);
    }

    void originSet(RestoreImpl backup, Destination source) {
        debug(sourceSet, source, backup);
    }

    void clientSet(RestoreImpl backup, Client client) {
        debug(clientSet, client, backup);
    }

	void dryrunSet(RestoreImpl backup, boolean dryrun) {
		debug(m.dryrunSet, dryrun, backup);
	}
}
