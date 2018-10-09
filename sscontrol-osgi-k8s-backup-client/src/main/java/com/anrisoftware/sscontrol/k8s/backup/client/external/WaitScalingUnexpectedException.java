package com.anrisoftware.sscontrol.k8s.backup.client.external;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WaitScalingUnexpectedException extends BackupClientException {

    public WaitScalingUnexpectedException(Throwable cause, Service service,
            int replicas) {
        super("Scaling deployment timeout", cause);
        addContextValue("service", service);
        addContextValue("replicas", replicas);
    }
}
