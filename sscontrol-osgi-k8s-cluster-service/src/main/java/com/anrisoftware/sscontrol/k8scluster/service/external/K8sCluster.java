package com.anrisoftware.sscontrol.k8scluster.service.external;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterTargetService;

/**
 * Kubernetes cluster target service. Contains the {@link Cluster} and
 * {@link Context}.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8sCluster extends ClusterTargetService {

    Cluster getCluster();

    Context getContext();

}
