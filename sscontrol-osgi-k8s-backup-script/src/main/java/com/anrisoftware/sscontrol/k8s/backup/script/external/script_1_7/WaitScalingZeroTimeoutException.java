package com.anrisoftware.sscontrol.k8s.backup.script.external.script_1_7;

import org.joda.time.Duration;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WaitScalingZeroTimeoutException extends AppException {

    public WaitScalingZeroTimeoutException(String namespace, String name,
            Duration timeout) {
        super("Scaling deployment to zero error");
        addContextValue("namespace", namespace);
        addContextValue("name", name);
        addContextValue("timeout", timeout);
    }
}
