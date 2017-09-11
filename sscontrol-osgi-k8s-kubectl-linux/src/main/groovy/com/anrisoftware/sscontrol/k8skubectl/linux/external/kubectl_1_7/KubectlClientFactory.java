package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7;

import com.anrisoftware.sscontrol.types.cluster.external.ClusterHost;

import io.fabric8.kubernetes.client.NamespacedKubernetesClient;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface KubectlClientFactory {

    KubectlClient create(ClusterHost cluster, Object parent);

    KubectlClient create(NamespacedKubernetesClient client, Object parent);
}
