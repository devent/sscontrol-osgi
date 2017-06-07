package com.anrisoftware.sscontrol.k8s.glusterfsheketi.external;

/**
 * Glusterfs-Heketi storage.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Storage {

    String getName();

    String getRestAddress();

    String getRestUser();

    String getRestKey();

    Boolean getIsDefault();
}
