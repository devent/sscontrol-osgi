package com.anrisoftware.sscontrol.k8scluster.service.external;

import java.util.Map;

/**
 * Kubernetes cluster service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8sClusterScript {

    void runKubectl(Map<String, Object> vars);

}
