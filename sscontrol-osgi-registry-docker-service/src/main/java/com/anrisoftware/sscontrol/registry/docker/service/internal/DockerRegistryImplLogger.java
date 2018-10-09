package com.anrisoftware.sscontrol.registry.docker.service.internal;

import static com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImplLogger.m.credentialsSet;
import static com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImplLogger.m.hostSet;
import static com.anrisoftware.sscontrol.registry.docker.service.internal.DockerRegistryImplLogger.m.registrySet;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.registry.docker.service.external.Credentials;
import com.anrisoftware.sscontrol.registry.docker.service.external.Host;
import com.anrisoftware.sscontrol.registry.docker.service.external.Registry;

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

        registrySet("Registry {} set for {}");

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

    void registrySet(DockerRegistryImpl docker, Registry registry) {
        debug(registrySet, registry, docker);
    }

}
