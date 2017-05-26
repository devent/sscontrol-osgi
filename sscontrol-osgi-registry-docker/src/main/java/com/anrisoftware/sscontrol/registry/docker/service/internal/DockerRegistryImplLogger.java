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
package com.anrisoftware.sscontrol.registry.docker.service.internal;

import static com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImplLogger.m.clientSet;
import static com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImplLogger.m.credentialsSet;
import static com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImplLogger.m.hostSet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.registry.docker.service.external.Client;
import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials;
import com.anrisoftware.sscontrol.registry.docker.service.external.Host;

/**
 * Logging for {@link DockerRegistryImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
final class DockerRegistryImplLogger extends AbstractLogger {

    enum m {

        hostSet("Host {} set for {}"),

        credentialsSet("Credentials {} set for {}"),

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
     * Sets the context of the logger to {@link DockerRegistryImpl}.
     */
    public DockerRegistryImplLogger() {
        super(DockerRegistryImpl.class);
    }

    void credentialsSet(DockerRegistryImpl registry, Credentials c) {
        debug(credentialsSet, c, registry);
    }

    void hostSet(DockerRegistryImpl registry, Host remote) {
        debug(hostSet, remote, registry);
    }

    void clientSet(DockerRegistryImpl registry, Client c) {
        debug(clientSet, c, registry);
    }

}
