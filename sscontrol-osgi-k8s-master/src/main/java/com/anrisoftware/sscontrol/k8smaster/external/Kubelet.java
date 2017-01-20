package com.anrisoftware.sscontrol.k8smaster.external;

import java.util.List;

/**
 * Kubelet client.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Kubelet {

    /**
     * Kubelet client TLS.
     */
    Tls getTls();

    /**
     * List of the preferred NodeAddressTypes.
     */
    List<String> getPreferredAddressTypes();

    Binding getBinding();
}
