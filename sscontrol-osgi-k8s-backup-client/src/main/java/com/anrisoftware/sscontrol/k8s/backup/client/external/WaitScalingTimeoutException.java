package com.anrisoftware.sscontrol.k8s.backup.client.external;

import org.joda.time.Duration;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WaitScalingTimeoutException extends BackupClientException {

    public WaitScalingTimeoutException(Service service, int replicas,
            Duration timeout) {
        super("Scaling deployment timeout");
        addContextValue("service", service);
        addContextValue("replicas", replicas);
        addContextValue("timeout", timeout);
    }

}
