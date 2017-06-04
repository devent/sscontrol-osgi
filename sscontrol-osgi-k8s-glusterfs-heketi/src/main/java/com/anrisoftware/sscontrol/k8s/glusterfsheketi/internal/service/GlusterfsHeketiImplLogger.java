/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.service;

import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.service.GlusterfsHeketiImplLogger.m.clustersAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.service.GlusterfsHeketiImplLogger.m.registriesAdded;
import static com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.service.GlusterfsHeketiImplLogger.m.reposAdded;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
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

        registriesAdded("Repositories {} added to {}");

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

    void clustersAdded(GlusterfsHeketiImpl from, List<ClusterHost> list) {
        debug(clustersAdded, list, from);
    }

    void reposAdded(GlusterfsHeketiImpl from, List<RepoHost> list) {
        debug(reposAdded, list, from);
    }

    void registriesAdded(GlusterfsHeketiImpl from, List<RegistryHost> list) {
        debug(registriesAdded, list, from);
    }
}
