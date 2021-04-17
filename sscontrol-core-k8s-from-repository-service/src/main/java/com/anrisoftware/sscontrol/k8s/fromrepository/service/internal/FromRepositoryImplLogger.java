/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.k8s.fromrepository.service.internal;

/*-
 * #%L
 * sscontrol-osgi - k8s-from-repository-service
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImplLogger.m.registriesAdded;
import static com.anrisoftware.sscontrol.k8s.fromrepository.service.internal.FromRepositoryImplLogger.m.reposAdded;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.k8s.fromrepository.service.external.Crd;
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

        dryrunSet("Dry-run {} set for {}"),

        crdsAdded("CRD {} added to {}");

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

    void crdsAdded(FromRepositoryImpl from, Crd crd) {
        debug(m.crdsAdded, crd, from);
    }
}
