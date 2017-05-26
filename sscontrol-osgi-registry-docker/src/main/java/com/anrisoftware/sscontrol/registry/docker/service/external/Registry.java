package com.anrisoftware.sscontrol.registry.docker.service.external;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Docker image registry.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Registry {

    Integer getPort();

    Tls getClient();

}
