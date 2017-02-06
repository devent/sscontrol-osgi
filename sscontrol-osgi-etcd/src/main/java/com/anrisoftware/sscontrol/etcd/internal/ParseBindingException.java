package com.anrisoftware.sscontrol.etcd.internal;

import java.net.URISyntaxException;
import java.util.Map;

import com.anrisoftware.sscontrol.types.external.AppException;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ParseBindingException extends AppException {

    public ParseBindingException(URISyntaxException e,
            Map<String, Object> args) {
        super("Parse binding error", e);
        addContextValue("args", args);
    }

}
