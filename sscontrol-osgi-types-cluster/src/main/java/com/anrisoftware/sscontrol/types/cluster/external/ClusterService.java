package com.anrisoftware.sscontrol.types.cluster.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.HostService;

/**
 * Cluster service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ClusterService extends HostService {

    /**
     * Returns the host that can be used to have access to the cluster via
     * kubectl.
     */
    ClusterHost getClusterHost();

    /**
     * Returns the hosts that can be used to have access to the clusters via
     * kubectl.
     */
    List<ClusterHost> getClusterHosts();

}
