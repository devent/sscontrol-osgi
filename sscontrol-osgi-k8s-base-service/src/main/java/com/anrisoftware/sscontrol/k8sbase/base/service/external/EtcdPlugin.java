package com.anrisoftware.sscontrol.k8sbase.base.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * <i>Etcd</i> plugin.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface EtcdPlugin extends Plugin {

    /**
     * Returns the list of endpoints.
     */
    List<Object> getEndpoints();

    Tls getTls();

    String getProtocol();

    Integer getPort();
}
