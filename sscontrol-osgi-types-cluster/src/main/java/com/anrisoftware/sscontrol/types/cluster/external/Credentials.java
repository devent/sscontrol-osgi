package com.anrisoftware.sscontrol.types.cluster.external;

/**
 * Cluster credentials.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Credentials {

    /**
     * Returns the credentials type.
     */
    String getType();

    /**
     * Returns the user name.
     */
    String getName();

    /**
     * Returns the cluster host.
     */
    String getHost();

    /**
     * Returns the cluster host port.
     */
    Integer getPort();
}
