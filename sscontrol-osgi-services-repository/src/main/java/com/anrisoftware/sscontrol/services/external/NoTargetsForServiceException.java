package com.anrisoftware.sscontrol.services.external;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.host.external.HostServiceService;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NoTargetsForServiceException extends AppException {

    public NoTargetsForServiceException(AssertionError e,
            HostServiceService service, String target) {
        super("No targets", e);
        addContextValue("target", target);
        addContextValue("service", service);
    }

}
