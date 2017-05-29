package com.anrisoftware.sscontrol.registry.docker.service.external;

import java.net.URI;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Docker image registry.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Registry {

    URI getAddress();

    Integer getPort();

    Tls getClient();

}
