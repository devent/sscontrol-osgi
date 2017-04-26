package com.anrisoftware.sscontrol.repo.git.external;

import com.anrisoftware.sscontrol.tls.external.Tls;
import com.anrisoftware.sscontrol.types.external.cluster.ClusterHost;

/**
 * <i>K8s</i> cluster host.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface K8sClusterHost extends ClusterHost {

    /**
     * Returns the client certificates or <code>null</code>.
     */
    Tls getTls();

    K8sCluster getCluster();

    Credentials getCredentials();

    Context getContext();
}
