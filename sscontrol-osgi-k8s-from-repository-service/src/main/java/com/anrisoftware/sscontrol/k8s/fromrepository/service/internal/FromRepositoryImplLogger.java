package com.anrisoftware.sscontrol.k8s.fromrepository.service.internal;

import static com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImplLogger.m.registriesAdded;
import static com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImplLogger.m.reposAdded;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * Logging for {@link FromRepositoryImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class FromRepositoryImplLogger extends AbstractLogger {

    enum m {

        clustersAdded("Clusters {} added to {}"),

        reposAdded("Repositories {} added to {}"),

        registriesAdded("Repositories {} added to {}"),

        destinationSet("Manifests destination {} set for {}"),

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
     * Sets the context of the logger to {@link FromRepositoryImpl}.
     */
    FromRepositoryImplLogger() {
        super(FromRepositoryImpl.class);
    }

    void clustersAdded(FromRepositoryImpl from, List<ClusterHost> list) {
        debug(clustersAdded, list, from);
    }

    void reposAdded(FromRepositoryImpl from, List<RepoHost> list) {
        debug(reposAdded, list, from);
    }

    void registriesAdded(FromRepositoryImpl from, List<RegistryHost> list) {
        debug(registriesAdded, list, from);
    }

    void destinationSet(FromRepositoryImpl from, String dest) {
        debug(m.destinationSet, dest, from);
    }

    void dryrunSet(FromRepositoryImpl from, boolean dryrun) {
        debug(m.dryrunSet, dryrun, from);
    }
}
