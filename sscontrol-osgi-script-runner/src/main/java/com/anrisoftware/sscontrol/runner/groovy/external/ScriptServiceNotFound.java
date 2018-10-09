package com.anrisoftware.sscontrol.runner.groovy.external;

import com.anrisoftware.sscontrol.types.app.external.AppException;
import com.anrisoftware.sscontrol.types.parser.external.Parser;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ScriptServiceNotFound extends AppException {

    public ScriptServiceNotFound(Parser parser, String serviceName) {
        super("Script service not found");
        addContextValue("service name", serviceName);
        addContextValue("parser", parser);
    }

}
