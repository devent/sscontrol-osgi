package com.anrisoftware.sscontrol.flanneldocker.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * <i>Flannel-Docker</i> Etcd.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface Etcd {

    List<Object> getEndpoints();

    /**
     * Returns the address to setup flanneld.
     */
    Object getAddress();

    String getPrefix();

    Tls getTls();
}
