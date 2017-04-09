package com.anrisoftware.sscontrol.k8scluster.external;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.external.ClusterHost;

/**
 * <i>K8s</i> cluster host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface K8sClusterHost extends ClusterHost {

    /**
     * Returns the credentials type.
     */
    String getType();

    /**
     * Returns the user name.
     */
    String getName();

    /**
     * Returns the client certificates or <code>null</code>.
     */
    Tls getTls();

    K8sCluster getCluster();

    Credentials getCredentials();
}
