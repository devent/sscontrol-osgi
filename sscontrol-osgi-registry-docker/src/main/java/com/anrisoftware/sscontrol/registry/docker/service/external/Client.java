package com.anrisoftware.sscontrol.registry.docker.service.external;

import java.net.URI;

/**
 * Client certificates.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Client {

    URI getCa();

    URI getCert();

    URI getKey();

    String getCaName();

    String getCertName();

    String getKeyName();
}
