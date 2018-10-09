package com.anrisoftware.sscontrol.k8smaster.service.external;

import java.net.URI;

/**
 * Simple local-file-based user-configured authorization policy.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface AbacAuthorization extends Authorization {

    URI getFile();

    String getAbac();
}
