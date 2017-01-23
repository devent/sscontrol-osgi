package com.anrisoftware.sscontrol.flanneldocker.external;

/**
 * <i>Flannel-Docker</i> Etcd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Etcd {

    String getAddress();

    String getPrefix();
}
