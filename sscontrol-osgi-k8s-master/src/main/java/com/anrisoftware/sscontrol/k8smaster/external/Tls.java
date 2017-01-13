package com.anrisoftware.sscontrol.k8smaster.external;

import java.net.URI;

/**
 * TLS certificates.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Tls {

    URI getCa();

    URI getCert();

    URI getKey();
}
