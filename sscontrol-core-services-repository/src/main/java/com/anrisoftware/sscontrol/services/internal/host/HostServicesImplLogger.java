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
package com.anrisoftware.sscontrol.services.internal.host;

import static com.anrisoftware.sscontrol.services.internal.host.HostServicesImplLogger.m.addService;
import static com.anrisoftware.sscontrol.services.internal.host.HostServicesImplLogger.m.availableServiceAdded;
import static com.anrisoftware.sscontrol.services.internal.host.HostServicesImplLogger.m.clustersInjected;
import static com.anrisoftware.sscontrol.services.internal.host.HostServicesImplLogger.m.registriesInjected;
import static com.anrisoftware.sscontrol.services.internal.host.HostServicesImplLogger.m.reposInjected;
import static com.anrisoftware.sscontrol.services.internal.host.HostServicesImplLogger.m.targetsInjected;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.HostService;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;
import com.anrisoftware.sscontrol.types.registry.external.RegistryHost;
import com.anrisoftware.sscontrol.types.repo.external.RepoHost;

/**
 * Logging for {@link HostServicesImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class HostServicesImplLogger extends AbstractLogger {

    enum m {

        addService("Service '{}':{} added to {}"),

        availableServiceAdded("Available service '{}':{} added to {}"),

        targetsInjected("Inject targets to {} targets {} from {}"),

        clustersInjected("Inject clusters to {} clusters {} from {}"),

        reposInjected("Inject repos to {} repos {} from {}"),

        registriesInjected("Inject registries to {} repos {} from {}");

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
     * Sets the context of the logger to {@link HostServicesImpl}.
     */
    public HostServicesImplLogger() {
        super(HostServicesImpl.class);
    }

    void addService(HostServicesImpl services, String name,
            HostService service) {
        debug(addService, name, service, services);
    }

    void availableServiceAdded(HostServicesImpl services, String name,
            HostServiceService service) {
        debug(availableServiceAdded, name, service, services);
    }

    void targetsInjected(HostServicesImpl services, String name,
            List<?> targets) {
        trace(targetsInjected, name, targets, services);
    }

    void clustersInjected(HostServicesImpl services, String name,
            List<ClusterHost> clusters) {
        trace(clustersInjected, name, clusters, services);
    }

    void reposInjected(HostServicesImpl services, String name,
            List<RepoHost> repos) {
        trace(reposInjected, name, repos, services);
    }

    void registriesInjected(HostServicesImpl services, String name,
            List<RegistryHost> registries) {
        trace(registriesInjected, name, registries, services);
    }

}
