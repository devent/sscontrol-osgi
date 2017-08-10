package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.net.URI;
import java.util.List;

import com.anrisoftware.sscontrol.types.cluster.external.Credentials;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Deployment {

    Deployment createClient();

    Object getDeployment(String namespace, String name);

    Object getService(String namespace, String name);

    void scaleDeployment(Object deploy, int replicas);

    void waitScaleUp(Object deploy);

    void waitScaleZero(Object deployOp);

    List<?> getPods(String namespace, String name);

    Object createPublicService(Object deploy);

    void deleteService(Object service);

    Object buildConfig(URI hostUrl, Credentials credentials);

    List<?> waitDeploy(Object deploy, int replicas, boolean ready);
}
