package com.anrisoftware.sscontrol.types.cluster.external;

import java.util.List;

import com.anrisoftware.sscontrol.types.host.external.TargetHostService;

/**
 * Cluster target service. Contains the {@link ClusterHost} that can be used to
 * connect to the cluster and the {@link Credentials} for authentication.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface ClusterTargetService extends TargetHostService<ClusterHost> {

    List<Credentials> getCredentials();

}
