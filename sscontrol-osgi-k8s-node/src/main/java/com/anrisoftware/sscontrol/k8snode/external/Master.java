package com.anrisoftware.sscontrol.k8snode.external;

/**
 * Kubernetes master.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Master {

    String getTarget();

    String getAddress();

    String getProtocol();

    Integer getPort();

}
