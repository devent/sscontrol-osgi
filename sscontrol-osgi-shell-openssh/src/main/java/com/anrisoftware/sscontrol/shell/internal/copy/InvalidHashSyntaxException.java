package com.anrisoftware.sscontrol.shell.internal.copy;

import java.net.URISyntaxException;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class InvalidHashSyntaxException extends AppException {

    public InvalidHashSyntaxException(URISyntaxException e, Object v) {
        super("Invalid hash syntax", e);
        addContextValue("hash", v.toString());
    }

}