package com.anrisoftware.sscontrol.k8s.backup.client.external;

import com.anrisoftware.sscontrol.k8scluster.external.K8sClusterHost;

import io.fabric8.kubernetes.client.NamespacedKubernetesClient;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface DeploymentFactory {

    Deployment create(K8sClusterHost host);

    Deployment create(K8sClusterHost host, NamespacedKubernetesClient client);
}
