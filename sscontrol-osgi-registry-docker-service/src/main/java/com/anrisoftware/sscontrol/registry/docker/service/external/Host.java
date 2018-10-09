package com.anrisoftware.sscontrol.registry.docker.service.external;

import java.net.URI;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Docker host.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Host {

    /**
     * Returns the host address.
     */
    URI getAddress();

    /**
     * Returns the client certificates.
     */
    Tls getClient();
}
