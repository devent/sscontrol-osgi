package com.anrisoftware.sscontrol.k8scluster.service.external;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * <i>K8s</i> cluster host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface K8sClusterHost extends ClusterHost {

    /**
     * Returns the client certificates for TLS connection.
     *
     * @return {@link Tls}
     */
    Tls getTls();

    /**
     * Returns the host target from where to run kubectl.
     *
     * @return {@link TargetHost}
     */
    TargetHost getTarget();

    /**
     * Returns information about the cluster.
     *
     * @return {@link K8sCluster}
     */
    K8sCluster getCluster();

}
