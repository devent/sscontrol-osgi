package com.anrisoftware.sscontrol.types.app.external;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AppException extends ContextedRuntimeException {

    protected AppException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AppException(String message) {
        super(message);
    }

}
