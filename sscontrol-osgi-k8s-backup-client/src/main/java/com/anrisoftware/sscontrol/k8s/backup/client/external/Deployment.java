package com.anrisoftware.sscontrol.k8s.backup.client.external;

import java.util.List;

import org.joda.time.Duration;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public interface Deployment {

    Service getService();

    int getReplicas();

    int getReadyReplicas();

    void scaleDeploy(int replicas);

    void waitScaleDeploy(int replicas, Duration timeout);

    void waitExposeDeploy(String name);

    /**
     * Returns the exposed node port for a service.
     *
     * @return the {@link Integer} node port or <code>null</code> if the service
     *         was not exposed.
     */
    Integer getNodePort(String name);

    void deleteService(String name);

    List<String> getPods();

    void execCommand(String... cmd);
}
