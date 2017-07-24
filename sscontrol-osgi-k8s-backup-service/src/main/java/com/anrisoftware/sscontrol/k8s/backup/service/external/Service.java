package com.anrisoftware.sscontrol.k8s.backup.service.external;

/**
 * Service for backup.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Service {

    /**
     * Returns the namespace.
     */
    String getNamespace();

    /**
     * Returns the name.
     */
    String getName();
}
