package com.anrisoftware.sscontrol.etcd.external;

/**
 * <i>Etcd</i> cluster.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Cluster {

    String getName();

    Binding getAddress();

}
