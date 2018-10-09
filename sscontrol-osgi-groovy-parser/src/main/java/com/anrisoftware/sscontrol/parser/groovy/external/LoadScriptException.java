package com.anrisoftware.sscontrol.parser.groovy.external;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.parser.external.Parser;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoadScriptException extends AppException {

    public LoadScriptException(Parser parser, Throwable e, String name) {
        super("Load script error", e);
        addContextValue("parser", parser);
        addContextValue("name", name);
    }

    public LoadScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadScriptException(String message) {
        super(message);
    }

}
