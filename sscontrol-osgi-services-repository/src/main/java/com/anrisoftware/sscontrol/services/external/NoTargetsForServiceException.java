package com.anrisoftware.sscontrol.services.external;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NoTargetsForServiceException extends AppException {

    public NoTargetsForServiceException(AssertionError e, String name) {
        super("No targets", e);
        addContextValue("name", name);
    }

}
