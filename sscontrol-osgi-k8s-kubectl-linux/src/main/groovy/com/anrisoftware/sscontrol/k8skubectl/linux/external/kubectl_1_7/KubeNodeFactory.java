package com.anrisoftware.sscontrol.k8skubectl.linux.external.kubectl_1_7;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface KubeNodeFactory {

    KubeNode create(NamespacedKubernetesClient client,
            KubeNodeResource resource, Node node, Object parent);
}
