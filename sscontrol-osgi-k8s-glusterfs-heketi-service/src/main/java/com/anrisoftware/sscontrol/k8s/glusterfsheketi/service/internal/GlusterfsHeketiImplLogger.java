package com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal;

import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImplLogger.m.nodeAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImplLogger.m.nodesAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImplLogger.m.registriesAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImplLogger.m.reposAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.internal.GlusterfsHeketiImplLogger.m.storageSet;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.glusterfsheketi.service.external.Storage;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * Logging for {@link GlusterfsHeketiImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class GlusterfsHeketiImplLogger extends AbstractLogger {

    enum m {

        clustersAdded("Clusters {} added to {}"),

        reposAdded("Repositories {} added to {}"),

        registriesAdded("Repositories {} added to {}"),

        nodesSet("Nodes group {} added to {}"),

        nodesAdded("Nodes added to {}: {}"),

        nodeAdded("Node {} added to {}"),

        storageSet("Storage {} set for {}");

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
     * Sets the context of the logger to {@link GlusterfsHeketiImpl}.
     */
    GlusterfsHeketiImplLogger() {
        super(GlusterfsHeketiImpl.class);
    }

    void clustersAdded(GlusterfsHeketiImpl g, List<ClusterHost> list) {
        debug(clustersAdded, list, g);
    }

    void reposAdded(GlusterfsHeketiImpl g, List<RepoHost> list) {
        debug(reposAdded, list, g);
    }

    void registriesAdded(GlusterfsHeketiImpl g, List<RegistryHost> list) {
        debug(registriesAdded, list, g);
    }

    void nodesAdded(GlusterfsHeketiImpl g, List<Object> nodes) {
        debug(nodesAdded, g, nodes);
    }

    void storageSet(GlusterfsHeketiImpl g, Storage storage) {
        debug(storageSet, storage, g);
    }

    void nodeAdded(GlusterfsHeketiImpl g, Object node) {
        debug(nodeAdded, node, g);
    }
}
