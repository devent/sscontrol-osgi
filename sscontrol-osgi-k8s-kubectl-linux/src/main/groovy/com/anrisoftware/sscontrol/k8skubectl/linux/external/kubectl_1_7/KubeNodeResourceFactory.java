package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7;

import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface KubeNodeResourceFactory {

    KubeNodeResource create(NamespacedKubernetesClient client,
            @SuppressWarnings("rawtypes") Resource resource);
}
