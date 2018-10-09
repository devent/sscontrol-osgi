package com.anrisoftware.sscontrol.k8smaster.service.external;

import java.net.UnknownHostException;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;

/**
 * Thrown if the host could not be found for the target.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class UnknownHostForTargetException extends AppException {

    public UnknownHostForTargetException(UnknownHostException e,
            TargetHost host) {
        super("Unknown host for target", e);
        addContextValue("host", host);
    }

}
