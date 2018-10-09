package com.anrisoftware.sscontrol.k8s.backup.client.external;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface DeploymentFactory {

    Deployment create(ClusterHost host, Object kubectl, Service service);
}
