package com.anrisoftware.sscontrol.k8scluster.service.external;

import com.anrisoftware.sscontrol.types.cluster.external.Credentials;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface K8sClusterHostFactory {

    K8sClusterHost create(K8sCluster cluster, TargetHost target,
            Credentials credentials, Context context);
}
