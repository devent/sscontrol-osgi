package com.anrisoftware.sscontrol.k8smaster.service.external;

import java.net.URI;

/**
 * X509 Client Certs.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface ClientCertsAuthentication extends Authentication {

    URI getCa();

    URI getCert();

    URI getKey();

    String getCaName();

    String getCertName();

    String getKeyName();
}
