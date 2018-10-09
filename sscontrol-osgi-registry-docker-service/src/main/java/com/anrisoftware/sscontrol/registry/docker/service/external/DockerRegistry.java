package com.anrisoftware.sscontrol.registry.docker.service.external;

/**
 * <i>Docker</i> registry service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DockerRegistry
        extends com.anrisoftware.sscontrol.types.registry.external.Registry {

    /**
     * Returns the docker host.
     */
    Host getHost();

    /**
     * Returns the docker registry.
     */
    Registry getRegistry();

    /**
     * Returns the docker registry credentials.
     */
    Credentials getCredentials();
}
