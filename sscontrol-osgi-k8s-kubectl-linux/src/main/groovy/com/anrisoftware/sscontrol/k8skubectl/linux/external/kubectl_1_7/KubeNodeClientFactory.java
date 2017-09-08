package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

import io.fabric8.kubernetes.client.NamespacedKubernetesClient;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface KubeNodeClientFactory {

    KubeNodeClient create(ClusterHost cluster);

    KubeNodeClient create(NamespacedKubernetesClient client);

}
