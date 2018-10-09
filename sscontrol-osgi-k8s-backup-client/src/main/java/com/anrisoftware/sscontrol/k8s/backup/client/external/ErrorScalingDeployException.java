package com.anrisoftware.sscontrol.k8s.backup.client.external;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ErrorScalingDeployException extends BackupClientException {

    public ErrorScalingDeployException(Service service, int replicas) {
        super("Error scaling deployment");
        addContextValue("service", service);
        addContextValue("replicas", replicas);
    }

}
