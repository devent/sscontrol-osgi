package com.anrisoftware.sscontrol.k8s.backup.service.internal;

import static com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImplLogger.m.clientSet;
import static com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImplLogger.m.destinationSet;
import static com.anrisoftware.sscontrol.k8s.backup.service.internal.BackupImplLogger.m.serviceSet;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Client;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Destination;
import com.anrisoftware.sscontrol.k8s.backup.client.external.Service;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

/**
 * Logging for {@link BackupImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class BackupImplLogger extends AbstractLogger {

    enum m {

        clustersAdded("Clusters {} added to {}"),

        serviceSet("Service {} set for {}"),

        destinationSet("Destination {} set for {}"),

        clientSet("Client {} set for {}");

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
     * Sets the context of the logger to {@link BackupImpl}.
     */
    BackupImplLogger() {
        super(BackupImpl.class);
    }

    void clustersAdded(BackupImpl from, List<ClusterHost> list) {
        debug(clustersAdded, list, from);
    }

    void serviceSet(BackupImpl backup, Service service) {
        debug(serviceSet, service, backup);
    }

    void destinationSet(BackupImpl backup, Destination dest) {
        debug(destinationSet, dest, backup);
    }

    void clientSet(BackupImpl backup, Client client) {
        debug(clientSet, client, backup);
    }
}
