package com.anrisoftware.sscontrol.k8scluster.external;

import java.util.Map;

/**
 * Kubernetes cluster service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface K8sClusterScript {

    void runKubectl(Map<String, Object> vars);

    void uploadCertificates(Map<String, Object> vars);

}
