package com.anrisoftware.sscontrol.k8sbase.base.service.external;

import java.util.List;

import com.anrisoftware.sscontrol.tls.external.Tls;

/**
 * Kubelet client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Kubelet {

    /**
     * Returns the address to bind kubelet to.
     *
     * @return {@link String}
     */
    String getAddress();

    /**
     * Returns the port to bind kubelet to.
     *
     * @return {@link Integer}
     */
    Integer getPort();

    /**
     * Kubelet TLS.
     */
    Tls getTls();

    /**
     * Kubelet client TLS.
     */
    Tls getClient();

    /**
     * List of the preferred NodeAddressTypes.
     */
    List<String> getPreferredAddressTypes();

}
