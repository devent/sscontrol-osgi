package com.anrisoftware.sscontrol.app.main.internal.args;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ParseRootPathException extends AppException {

    public ParseRootPathException(Exception e) {
        super("Error parse root path", e);
    }

}
