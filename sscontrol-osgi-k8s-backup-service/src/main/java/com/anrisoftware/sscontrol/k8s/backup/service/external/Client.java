package com.anrisoftware.sscontrol.k8s.backup.service.external;

import java.net.URI;

/**
 * Backup client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Client {

    /**
     * Returns the private SSH key.
     */
    URI getKey();

    /**
     * Returns the client configuration.
     */
    String getConfig();

    Boolean getProxy();
}
